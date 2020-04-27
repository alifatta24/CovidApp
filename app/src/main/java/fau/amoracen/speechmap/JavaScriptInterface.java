package fau.amoracen.speechmap;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.ArrayList;

public class JavaScriptInterface {
    Context mContext;


    /** Instantiate the interface and set the context */
    JavaScriptInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public String updateDataDate(String datadate) {
        return "['3/22/20','3/23/20','3/24/20','3/25/20','3/26/20','3/27/20','3/28/20','3/29/20','3/30/20','3/31/20','4/1/20','4/2/20','4/3/20','4/4/20','4/5/20','4/6/20','4/7/20','4/8/20','4/9/20','4/10/20','4/11/20','4/12/20','4/13/20','4/14/20','4/15/20','4/16/20','4/17/20','4/18/20','4/19/20','4/20/20']";
    }

    @JavascriptInterface
    public String updateDataNum(String datanum) {
        return "[417,557,706,942,1209,1581,2026,2467,2978,3873,4757,5926,7087,8407,9619,10783,12794,14695,16544,18586,20462,22019,23528,25831,28325,32916,36773,38664,40661,42094]";
    }

}