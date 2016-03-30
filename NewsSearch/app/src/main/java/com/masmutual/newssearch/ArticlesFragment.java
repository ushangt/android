package com.masmutual.newssearch;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends Fragment {

    public static ArrayList<Article> articles = new ArrayList<>();
    ArticleAdapter articleAdapter = new ArticleAdapter();


    public ArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArticlesFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);
        ListView articlesListView = (ListView)rootView.findViewById(R.id.listViewArticles);
        articlesListView.setAdapter(articleAdapter);

        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity().getApplicationContext(), ScrollingContentActivity.class);
                i.putExtra("article",position);
                startActivity(i);

            }
        });

        return rootView;
    }

    public void getArticlesFromServer(){

        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyyMMdd").format(date);
        String url = AppConstants.NY_TIMES_BASE_URL+"search/v2/articlesearch.json?begin_date="+modifiedDate
                +"&end_date="+modifiedDate+"&sort=newest&api-key="+AppConstants.SEARCH_API_KEY;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            response = response.getJSONObject("response");
                            JSONArray docs = response.getJSONArray("docs");
                            for(int i =0;i <docs.length();i++){
                                Article temp = new Article();
                                JSONObject j = (JSONObject)docs.get(i);
                                temp.setHeadline(j.getJSONObject("headline").getString("main"));
                                String dateOfPublish = j.getString("pub_date");
                                String[] splittedDate = dateOfPublish.split("T");
                                temp.setPosted(splittedDate[0]+" "+splittedDate[1].substring(0,splittedDate[1].length()-1));
                                if(j.getJSONArray("multimedia").length()>0)
                                {
                                    JSONArray imageArray = j.getJSONArray("multimedia");
                                    for(int k=0;k<imageArray.length();k++){
                                        JSONObject jtemp = (JSONObject)imageArray.get(k);
                                        if(jtemp.getString("subtype").equals("thumbnail")){
                                            temp.setImageURL(java.net.URLDecoder.decode(jtemp.getString("url"),"UTF-8"));
                                        }
                                    }
                                }
                                temp.setContent(j.getString("lead_paragraph"));
                                articles.add(temp);
                            }

                            articleAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);

    }

    public class ArticleAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Object getItem(int position) {
            return articles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final ViewHolder mHolder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_articles_list_view,null);
                mHolder = new ViewHolder();
                mHolder.headlines = (TextView) convertView.findViewById(R.id.headlinesTextView);
                mHolder.dateView = (TextView) convertView.findViewById(R.id.dateTextView);
                mHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(mHolder);
            }
            else{
                mHolder = (ViewHolder) convertView.getTag();
            }

            if(articles.get(position).getImageURL() != null){

                String url = AppConstants.IMAGE_BASE_URL+articles.get(position).getImageURL();
                mHolder.imageView.setImageResource(R.drawable.placeholder);
                ImageRequest imgRequest = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                mHolder.imageView.setMinimumHeight(144);
                                mHolder.imageView.setMinimumWidth(144);
                                mHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                mHolder.imageView.setImageBitmap(response);
                            }
                        }, 0, 0, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHolder.imageView.setBackgroundColor(Color.parseColor("#ff0000"));
                        error.printStackTrace();
                    }
                });
                Volley.newRequestQueue(getActivity()).add(imgRequest);
            }
            else{
                mHolder.imageView.setImageResource(R.drawable.placeholder);
            }

            mHolder.headlines.setText(articles.get(position).getHeadline());
            mHolder.dateView.setText(articles.get(position).getPosted());
            return convertView;

        }
    }

    private class ViewHolder{
        private TextView headlines;
        private TextView dateView;
        private ImageView imageView;
    }

}
