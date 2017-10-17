package com.security.news.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.security.news.R;
import com.security.news.activity.MainActivity;
import com.security.news.bean.NewsBean;
import com.security.news.pager.NewsCenterPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单页Fragment;
 */
public class MenuFragment extends BaseFragment {

    private ListView listView;
    //左侧菜单的数据;
    private List<NewsBean.DataBean> datas=new ArrayList<>();
    private int selectedIndex;//ListView选中的位置;
    private MyAdapter adapter;

    @Override
    public View initViews() {
        View view=View.inflate(activity, R.layout.fragment_menu,null);
        listView = (ListView) view.findViewById(R.id.lv_menu);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex=position;
                adapter.notifyDataSetChanged();
                MainActivity mainActivity= (MainActivity) activity;
                if(listener!=null){
                    listener.switchPage(position);
                }else{
                    ContentFragment contentFragment= (ContentFragment) mainActivity.getFragmentFromTag(MainActivity.FRAMELAYOUT_CONTENT);
                    NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
                    newsCenterPager.switchPager(position);
                }
                mainActivity.toggleDrawer();
            }
        });
    }

    /**
     * 为左侧菜单设置数据;
     * @param datas
     */
    public void setMenuData(List<NewsBean.DataBean> datas) {
        this.datas=datas;
        adapter.notifyDataSetChanged();
    }


    /**
     * ListView的适配器;
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(datas!=null&&datas.size()>0){
                return datas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(activity,R.layout.lv_left_menu_item,null);
                holder=new ViewHolder();
                holder.tv_title=(TextView)convertView.findViewById(R.id.tv_left_menu);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            NewsBean.DataBean data= datas.get(position);
            holder.tv_title.setText(data.getTitle());
            //设置是否选中;
            if(selectedIndex==position){
                holder.tv_title.setEnabled(true);
            }else{
                holder.tv_title.setEnabled(false);
            }
            return convertView;
        }

        private class ViewHolder{
            private TextView tv_title;
        }
    }


    /*************接口回调****************/
    public interface OnSwitchPageListener{
        void switchPage(int position);
    }

    private OnSwitchPageListener listener;

    public void setOnSwitchPageListener(OnSwitchPageListener listener){
        this.listener=listener;
    }
}
