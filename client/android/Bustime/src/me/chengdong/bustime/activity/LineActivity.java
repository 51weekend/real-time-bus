package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.LineInfoAdapter;
import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class LineActivity extends BaseActivity implements OnItemClickListener {

    private final static String TAG = LineActivity.class.getSimpleName();

    ImageView mSearchClear;

    EditText mLineEdittext;

    ListView lineListView;

    TbConfigHandler tbConfigHandler = new TbConfigHandler(LineActivity.this);

    private String lineNumber;

    private LineInfoAdapter mLineAdapter;
    private final List<Line> mLineList = new ArrayList<Line>(0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line);

        mSearchClear = (ImageView) findViewById(R.id.iv_search_clear);

        mLineEdittext = (EditText) findViewById(R.id.line);

        lineListView = (ListView) findViewById(R.id.line_info_listview);

        mSearchClear.setOnClickListener(this);

        mLineEdittext.setSingleLine(true);

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

        mLineAdapter = new LineInfoAdapter(LineActivity.this, mLineList);
        lineListView.setAdapter(mLineAdapter);
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
            mLineAdapter.notifyDataSetChanged();
        }
    }

}
