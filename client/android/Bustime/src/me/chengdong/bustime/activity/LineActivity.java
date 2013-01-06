package me.chengdong.bustime.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.LineInfoAdapter;
import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.module.DownloadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class LineActivity extends BaseActivity implements OnItemClickListener {

    private final static String TAG = LineActivity.class.getSimpleName();

    ImageView mSearchClear;

    EditText mLineEdittext;

    ListView lineListView;

    Button mSearchBtn;

    TbConfigHandler tbConfigHandler = new TbConfigHandler(LineActivity.this);

    private String lineNumber;

    private LineInfoAdapter mAdapter;
    private final List<Line> mLineList = new ArrayList<Line>(0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line);

        mSearchClear = (ImageView) findViewById(R.id.iv_search_clear);
        mSearchClear.setOnClickListener(this);

        mSearchBtn = (Button) this.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);

        lineListView = (ListView) findViewById(R.id.line_info_listview);

        mLineEdittext = (EditText) findViewById(R.id.line);
        mLineEdittext.setSingleLine(true);
        mLineEdittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        mLineEdittext.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(mLineEdittext.getText().toString())) {
                    mSearchClear.setVisibility(View.GONE);
                } else {
                    if (StringUtil.isEmpty(mLineEdittext.getText().toString())) {
                        return;
                    }

                    lineNumber = mLineEdittext.getText().toString();
                    new QueryLineTask().execute();
                    mSearchClear.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });

        lineListView.setCacheColorHint(0);

        mAdapter = new LineInfoAdapter(LineActivity.this, mLineList);
        lineListView.setAdapter(mAdapter);
        lineListView.setOnItemClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLineList != null && mLineList.size() > 0) {
            return;
        }
        String savedLineEditData = tbConfigHandler.getLineNumber();
        if (StringUtil.isEmpty(lineNumber) && !StringUtil.isEmpty(savedLineEditData)) {
            lineNumber = savedLineEditData;
            mLineEdittext.setText(lineNumber);
            mLineEdittext.clearFocus();
        }
    }

    public void onPause() {
        super.onPause();
        if (StringUtil.isEmpty(mLineEdittext.getText().toString())) {
            return;
        }

        tbConfigHandler.saveOrUpdateLineNumber(mLineEdittext.getText().toString());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.iv_search_clear:
            mLineEdittext.setText("");
            break;
        case R.id.search_btn:
            new QueryLineFromServerTask().execute();
            break;
        default:
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

        Line line = this.mLineList.get((int) id);
        if (line == null) {
            LogUtil.d(TAG, "line info is null ");
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, SingleLineActivity.class);
        intent.putExtra(ParamUtil.LINE_GUID, line.getLineGuid());
        intent.putExtra(ParamUtil.LINE_NUMBER, line.getLineNumber());
        setIntent(intent);
        startActivity(intent);
    }

    private class QueryLineTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (StringUtil.isEmpty(lineNumber)) {
                    return null;
                }

                TbLineHandler lineHandler = new TbLineHandler(LineActivity.this);
                List<Line> lines = lineHandler.selectList(lineNumber);

                mLineList.clear();
                mLineList.addAll(lines);

            } catch (Exception e) {
                LogUtil.e(TAG, "获取数据出错", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class QueryLineFromServerTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            if (StringUtil.isEmpty(lineNumber)) {
                return;
            }
            openProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (StringUtil.isEmpty(lineNumber)) {
                    return null;
                }
                String name = URLEncoder.encode(lineNumber, "utf-8");
                ResultData result = DownloadData.getLine(LineActivity.this, URLEncoder.encode(name, "utf-8"));
                if (result.success()) {
                    @SuppressWarnings("unchecked")
                    List<Line> temps = (List<Line>) result.getData();
                    mLineList.clear();
                    mLineList.addAll(temps);

                    TbLineHandler tbLineHandler = new TbLineHandler(LineActivity.this);
                    for (Line line : temps) {
                        tbLineHandler.saveOrUpdate(line);
                    }
                } else {
                    // TODO 进行错误提示
                }

            } catch (Exception e) {
                LogUtil.e(TAG, "decode error", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            closeProgressDialog();
            mAdapter.notifyDataSetChanged();
        }
    }

}
