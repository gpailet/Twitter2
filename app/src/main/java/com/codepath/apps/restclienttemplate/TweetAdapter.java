package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by gpailet on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    public List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;

    //define an interface required by the ViewHolder
    public interface TweetAdapterListener{
        public void onItemSelected(View view,int position);
        public void onProfileSelected(View view,int position);
        public void onLikeSelected(View view,int position);
        public void onRetweetSelected(View view,int position);
        public void onReplySelected(View view,int position);
    }

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets,TweetAdapterListener listener){

        mTweets=tweets;
        mListener=listener;
    }
    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        View tweetView=inflater.inflate(R.layout.item_tweet,parent,false);
        ViewHolder viewHolder=new ViewHolder(tweetView);
        return viewHolder;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //get the data according to the position
        Tweet tweet = mTweets.get(position);
        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvScreenName.setText(" @"+tweet.user.screenName);
        holder.tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);


        //Sets statuses of like
        if (tweet.favorite_status){
            holder.ivLike.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_heart));
        }
        else {
            holder.ivLike.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_heart_stroke));
        }

        if (tweet.retweet_status){
            holder.ivRetweet.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_retweet));
        }
        else {
            holder.ivRetweet.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_retweet_stroke));
        }


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvScreenName;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public ImageView ivReply;
        public ImageView ivRetweet;
        public ImageView ivLike;
        public ImageView ivDirectMessage;

        public ViewHolder(View itemView){
            super(itemView);

            // perform findViewByID lookups

            ivProfileImage=(ImageView)itemView.findViewById(R.id.ivProfileImage);
            ivReply=(ImageView)itemView.findViewById(R.id.ivReply);
            ivRetweet=(ImageView) itemView.findViewById(R.id.ivRetweet);
            ivLike=(ImageView) itemView.findViewById(R.id.ivLike);
            ivDirectMessage=(ImageView)itemView.findViewById(R.id.ivDirectMessage);
            tvUsername=(TextView)itemView.findViewById(R.id.tvUserName);
            tvBody=(TextView)itemView.findViewById(R.id.tvBody);
            tvScreenName=(TextView)itemView.findViewById(R.id.tvScreenName);
            tvCreatedAt=(TextView)itemView.findViewById(R.id.tvCreatedAt);

            // handle row click event
            tvBody.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        //get the position of row element
                        int position=getAdapterPosition();
                        //fire the listener callback
                        mListener.onItemSelected(v,position);
                    }

                }
            });

            // handle profile image click event
            ivProfileImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        // get the  position of row element
                        int position=getAdapterPosition();
                        mListener.onProfileSelected(v,position);
                    }
                }
            });
            ivLike.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        int position=getAdapterPosition();
                        Tweet tweet=mTweets.get(position);
                        if (tweet.favorite_status) {
                            ivLike.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_heart_stroke));
                        }
                        else{
                            ivLike.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_heart));
                        }
                        mListener.onLikeSelected(v,position);
                    }
                }
            });
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        int position=getAdapterPosition();
                        Tweet tweet=mTweets.get(position);
                        if (tweet.retweet_status) {
                            ivRetweet.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_retweet_stroke));
                        }
                        else{
                            ivRetweet.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_vector_retweet));
                        }
                        mListener.onRetweetSelected(v,position);
                    }
                }
            });
            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        int position=getAdapterPosition();
                        mListener.onReplySelected(v,position);
                    }
                }
            });

        }
        @Override
        public void onClick(View v){
            int position=getAdapterPosition();
            if (position!=RecyclerView.NO_POSITION){
                Tweet tweet=mTweets.get(position);

                Intent intent=new Intent(context, TweetDetailsActivity.class);

                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));

                context.startActivity(intent);
            }
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;}
}
