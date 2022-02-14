package id.abypraya.personalassignment.ui.Component.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private final String info, title, checklistInfo;
    private Dialog dialog;
    private MaterialButton btnOkPrompt, btnCancelPrompt;
    private Activity activities;
    private TextView txtTitlePrompt, txtDescriptionPrompt, txtCheckBoxInfo;
    private ImageView imgPrompt;
    private Drawable drawableImgPrompt;
    private CheckBox checkboxPrompt;
    private String methodApi;
    private ArrayList<String> getData = new ArrayList<>();

    public CustomDialog(Activity activity, String title, String info, String checklistInfo, Drawable drawableImgPrompt, ArrayList<String> getData, String methodApi) {
        super(activity);
        this.activities = activity;
        this.title = title;
        this.info = info;
        this.checklistInfo = checklistInfo;
        this.drawableImgPrompt = drawableImgPrompt;
        this.getData = getData;
        this.methodApi = methodApi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btnOkPrompt = (MaterialButton) findViewById(R.id.btn_okay);
        btnCancelPrompt = (MaterialButton) findViewById(R.id.btn_cancel);
        txtTitlePrompt = (TextView) findViewById(R.id.txtTitlePrompt);
        txtDescriptionPrompt = (TextView) findViewById(R.id.txtDescriptionPrompt);
        txtCheckBoxInfo = (TextView) findViewById(R.id.txtCheckBoxInfo);
        imgPrompt = (ImageView) findViewById(R.id.imgPrompt);
        checkboxPrompt = (CheckBox) findViewById(R.id.checkboxPrompt);

        txtTitlePrompt.setText(title);
        txtDescriptionPrompt.setText(info);
        txtCheckBoxInfo.setText(checklistInfo);

        btnOkPrompt.setOnClickListener(this);
        btnCancelPrompt.setOnClickListener(this);

        imgPrompt.setImageDrawable(drawableImgPrompt);
    }

    @Override
    public void onClick(View buttons) {
        switch (buttons.getId()){
            case R.id.btn_okay:
                if(checkboxPrompt.isChecked()){
                    Utility utility = new Utility();
                    ArrayList<String> data = getData;

                    if(methodApi.equals(ContentMenu.ADD_DETAIL_KELAS)){
                        utility.saveDataDetailKelas(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.ADD_PESERTA)){
                        utility.saveDataPeserta(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.ADD_INSTANSI)){
                        utility.saveDataInstansi(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.ADD_INSTRUKTUR)){
                        utility.saveDataInstruktur(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.ADD_MATERI)){
                        utility.saveDataMateri(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.ADD_KELAS)){
                        utility.saveDataKelas(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.UPDATE_KELAS)){
                        utility.updateDataKelas(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.UPDATE_MATERI)){
                        utility.updateDataMateri(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.UPDATE_INSTRUKTUR)){
                        utility.updateDataInstruktur(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.UPDATE_INSTANSI)){
                        utility.updateDataInstansi(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.UPDATE_PESERTA)){
                        utility.updateDataPeserta(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.UPDATE_DETAIL_KELAS)){
                        utility.updateDetailKelas(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.DELETE_KELAS)){
                        utility.deleteDataKelas(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.DELETE_INSTANSI)){
                        utility.deleteDataInstansi(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.DELETE_INSTRUKTUR)){
                        utility.deleteDataInstruktur(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.DELETE_MATERI)){
                        utility.deleteDataMateri(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.DELETE_PESERTA)){
                        utility.deleteDataPeserta(activities, data);
                    }
                    if(methodApi.equals(ContentMenu.DELETE_DETAIL_KELAS)){
                        utility.deleteDetailKelas(activities, data);
                    }
                }else{
                    String err = ContentMenu.PROMPT_UNCHECK_CHECKBOX;
                    txtCheckBoxInfo.requestFocus();
                    txtCheckBoxInfo.setError(err);
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

}
