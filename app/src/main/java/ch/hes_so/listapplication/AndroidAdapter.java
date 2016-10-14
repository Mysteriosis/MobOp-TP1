package ch.hes_so.listapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Iosis on 29.09.16.
 */

public class AndroidAdapter extends ArrayAdapter<AndroidVersion> {
    private ArrayList<AndroidVersion> androidVersionList;  private Context context;
    private int viewRes;
    private Resources res;

    public AndroidAdapter(Context context, int textViewResourceId, ArrayList<AndroidVersion> versions)
    {
        super(context, textViewResourceId, versions);
        this.androidVersionList = versions;
        this.context = context;
        this.viewRes = textViewResourceId;
        this.res = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(viewRes, parent, false);
        }

        final AndroidVersion androidVersion = this.androidVersionList.get(position);

        if (androidVersion != null) {
            final TextView title = (TextView) view.findViewById(R.id.title);
            final TextView description = (TextView) view.findViewById(R.id.description);
            final String versionName = String.format(res.getString(R.string.list_title), androidVersion.getVersionName());
            title.setText(versionName);
            final String versionNumber = String.format(res.getString(R.string.list_desc), androidVersion.getVersionNumber());
            description.setText(versionNumber);
        }

        return view;
    }
}
