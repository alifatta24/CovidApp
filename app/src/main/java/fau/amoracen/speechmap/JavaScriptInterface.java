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
    //get dates
    @JavascriptInterface
    public String updateDataDate(String datadate) {
        return "['3/31/20','4/1/20','4/2/20','4/3/20','4/4/20','4/5/20','4/6/20','4/7/20','4/8/20','4/9/20','4/10/20','4/11/20','4/12/20','4/13/20','4/14/20','4/15/20','4/16/20','4/17/20','4/18/20','4/19/20','4/20/20','4/21/20','4/22/20','4/23/20','4/24/20','4/25/20','4/26/20','4/27/20','4/28/20','4/29/20']";
    }
    //get data
    @JavascriptInterface
    public String updateDataNum(String datanum) {
        return "[863184,940523,1020920,1117272,1201186,1274854,1346822,1429982,1514776,1600590,1694667,1775429,1847796,1919174,1992903,2076502,2161885,2249004,2330764,2406786,2480741,2556720,2637439,2722857,2828682,2919404,2993292,3059944,3136506,3218184]";
    }

}