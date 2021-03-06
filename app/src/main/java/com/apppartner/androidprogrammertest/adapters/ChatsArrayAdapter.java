package com.apppartner.androidprogrammertest.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apppartner.androidprogrammertest.ImageDownloaderTask;
import com.apppartner.androidprogrammertest.R;
import com.apppartner.androidprogrammertest.models.ChatData;

import java.util.List;

/**
 * Created on 12/23/14.
 *
 * @author Thomas Colligan
 */
public class ChatsArrayAdapter extends ArrayAdapter<ChatData>
{
    public ChatsArrayAdapter(Context context, List<ChatData> objects)
    {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ChatCell chatCell = new ChatCell();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.cell_chat, parent, false);

        chatCell.usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextView);
        chatCell.messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        chatCell.chatImageView = (ImageView) convertView.findViewById(R.id.chatImageView);

        Typeface custom_font_head = Typeface.createFromAsset(getContext().getAssets(),"fonts/machinato.ttf");
        Typeface custom_font_body = Typeface.createFromAsset(getContext().getAssets(),"fonts/machinatoLight.ttf");
        chatCell.usernameTextView.setTypeface(custom_font_head);
        chatCell.messageTextView.setTypeface(custom_font_body);

        ChatData chatData = getItem(position);

        chatCell.usernameTextView.setText(chatData.username);
        chatCell.messageTextView.setText(chatData.message);

        if (chatCell.chatImageView != null) {
            new ImageDownloaderTask(chatCell.chatImageView).execute(chatData.avatarURL);
        }


        return convertView;
    }

    private static class ChatCell
    {
        TextView usernameTextView;
        TextView messageTextView;
        ImageView chatImageView;
    }
}
