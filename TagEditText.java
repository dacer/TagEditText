package im.dacer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Dacer on 5/23/14.
 * Show a drop-down list after input '#' symbol.
 */
public class TagEditText extends AutoCompleteTextView {

    private ArrayAdapter<String> adapter;
    public static final String TAG_END_WITH_REGULAR_EXPRESSION = "#\\S*\\z";

    public TagEditText(Context context){
        this(context, null);
    }
    public TagEditText(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }
    public TagEditText(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        setAdapter(adapter);
        setDropDownWidth(WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setTagList(String[] tags){
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, tags);
        setAdapter(adapter);
    }

    @Override
    public boolean enoughToFilter() {
        if(getText() != null){
            return getText().length() != 0;
        }
        return true;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode){
        String beforeCursor = getText().toString().substring(0, getSelectionStart());
        Pattern pattern = Pattern.compile(TAG_END_WITH_REGULAR_EXPRESSION);
        Matcher matcher = pattern.matcher(beforeCursor);
        if (matcher.find()) {
            text = matcher.group(0);;
        }
        super.performFiltering(text, keyCode);
    }

    @Override
    protected void replaceText (CharSequence text){
        String beforeCursor = getText().toString().substring(0, getSelectionStart());
        String afterCursor = getText().toString().substring(getSelectionStart());

        Pattern pattern = Pattern.compile("#\\S*");
        Matcher matcher = pattern.matcher(beforeCursor);
        StringBuffer sb = new StringBuffer();
        int matcherStart = 0;
        while (matcher.find()) {
            int curPos = getSelectionStart();
            if(curPos > matcher.start() &&
                    curPos <= matcher.end()){
                matcherStart = matcher.start();
                matcher.appendReplacement(sb, text.toString()+" ");
            }
        }
        matcher.appendTail(sb);
        setText(sb.toString()+afterCursor);
        setSelection(matcherStart + text.length()+1);
    }

}
