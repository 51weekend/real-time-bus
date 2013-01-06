package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.SingleLineAdapter;
import me.chengdong.bustime.db.TbFavoriteHandler;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.db.TbSingleLineHandler;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.CodeValue;
import me.chengdong.bustime.model.Favorite;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.SingleLine;
import me.chengdong.bustime.module.DownloadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.TipUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SingleLineActivity extends BaseActivity {

    private static final String TAG = SingleLineActivity.class.getSimpleName();

    private String lineGuid;

    private SingleLineAdapter mAdapter;

    ListView singleLineListView;

    TextView mTitle;

    Button mBackBtn;
    ImageButton mFavoriteBtn, mRefreshBtn;
    RelativeLayout mFavoriteLayout, mRefreshLayout;
    TextView mFavoriteText, mRefreshText;

    TbFavoriteHandler tbFavoriteHandler = new TbFavoriteHandler(SingleLineActivity.this);
    TbLineHandler tbLineHandler = new TbLineHandler(SingleLineActivity.this);
    TbSingleLineHandler tbSingleLineHandler = new TbSingleLineHandler(SingleLineActivity.this);

    private final List<SingleLine> mSingleLineList = new ArrayList<SingleLine>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_line);

        singleLineListView = (ListView) findViewById(R.id.single_line_listview);
        singleLineListView.setDivider(null);
        singleLineListView.setDividerHeight(0);

        mTitle = (TextView) findViewById(R.id.title_textview);

        // mChangeErroBtn = (Button) findViewById(R.id.change_error);
        //mChangeErroBtn.setOnClickListener(this);

        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);

        mFavoriteBtn = (ImageButton) findViewById(R.id.btn_favorite);
        mFavoriteBtn.setOnClickListener(this);
        mFavoriteLayout = (RelativeLayout) findViewById(R.id.btn_favorite_layout);
        mFavoriteLayout.setOnClickListener(this);
        mFavoriteText = (TextView) findViewById(R.id.tv_favorite);
        mFavoriteText.setOnClickListener(this);

        mRefreshBtn = (ImageButton) findViewById(R.id.btn_refresh);
        mRefreshBtn.setOnClickListener(this);
        mRefreshLayout = (RelativeLayout) findViewById(R.id.btn_refresh_layout);
        mRefreshLayout.setOnClickListener(this);
        mRefreshText = (TextView) findViewById(R.id.tv_refresh);
        mRefreshText.setOnClickListener(this);

        mLoadDialog = new ProgressDialog(this);
        mLoadDialog.setMessage("正在查询车次动态信息...");
        mAdapter = new SingleLineAdapter(SingleLineActivity.this, mSingleLineList);
        singleLineListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        lineGuid = intent.getStringExtra(ParamUtil.LINE_GUID);
        mTitle.setText(intent.getStringExtra(ParamUtil.LINE_NUMBER));
        openProgressDialog();
        new QuerySingleLineTask(true).execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.back_btn:
            this.finish();
            break;
        case R.id.btn_refresh:
        case R.id.tv_refresh:
        case R.id.btn_refresh_layout:
            mLoadDialog.show();
            new QuerySingleLineTask(false).execute();
            break;
        case R.id.btn_favorite:
        case R.id.tv_favorite:
        case R.id.btn_favorite_layout:
            Favorite favorite = new Favorite();
            List<Line> lines = tbLineHandler.selectListByManyGuid("'" + lineGuid + "'");
            if (lines.size() == 0) {
                break;
            }
            Line line = lines.get(0);
            favorite.setCode(lineGuid);
            favorite.setName(line.getLineNumber());
            favorite.setPropertyOne(line.getStartStation());
            favorite.setPropertyTwo(line.getEndStation());
            favorite.setPropertyThree(line.getRunTime());
            favorite.setType(FavoriteType.LINE.getType());

            tbFavoriteHandler.saveOrUpdate(favorite);

            Toast.makeText(this, "收藏线路信息成功", Toast.LENGTH_SHORT).show();
            break;
        default:
            break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_line, menu);
        return true;
    }

    private class QuerySingleLineTask extends AsyncTask<Void, Void, Void> {
        private boolean loadAllData = false;

        public QuerySingleLineTask(boolean loadAllData) {
            this.loadAllData = loadAllData;
        }

        @Override
        public void onPreExecute() {
            openProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ResultData result = new ResultData();
                List<SingleLine> localData = tbSingleLineHandler.selectList(lineGuid);
                if (localData.size() > 0) {
                    mSingleLineList.clear();
                    mSingleLineList.addAll(localData);
                    loadAllData = false;
                }
                if (loadAllData) {
                    result = DownloadData.getSingleLine(SingleLineActivity.this, lineGuid);
                } else {
                    result = DownloadData.getRunSingleLine(SingleLineActivity.this, lineGuid);
                }
                if (result.success()) {
                    if (loadAllData) {
                        @SuppressWarnings("unchecked")
                        List<SingleLine> temps = (List<SingleLine>) result.getData();
                        mSingleLineList.clear();
                        mSingleLineList.addAll(temps);

                        if (tbSingleLineHandler.exist(lineGuid)) {
                            return null;
                        }
                        for (SingleLine singleLine : temps) {
                            singleLine.setLineGuid(lineGuid);
                            tbSingleLineHandler.save(singleLine);
                        }

                    } else {
                        for (SingleLine singleLine : mSingleLineList) {
                            singleLine.setTime("");
                        }
                        @SuppressWarnings("unchecked")
                        List<CodeValue> temps = (List<CodeValue>) result.getData();
                        for (SingleLine singleLine : mSingleLineList) {
                            for (CodeValue codeValue : temps) {
                                if (codeValue.getCode().equals(singleLine.getStandCode())) {
                                    singleLine.setTime(codeValue.getValue());
                                }

                            }
                        }
                    }
                } else {
                    // TODO 提示错误给用户
                    TipUtil.tipDescription(SingleLineActivity.this, result.getMessage());
                }

            } catch (Exception e) {
                LogUtil.e(TAG, "获取数据出错", e);
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
