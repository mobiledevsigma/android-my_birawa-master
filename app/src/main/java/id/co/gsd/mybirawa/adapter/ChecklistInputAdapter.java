package id.co.gsd.mybirawa.adapter;

/**
 * Created by LENOVO on 09/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.controller.checklist.ChecklistSecondTabChildFragment;
import id.co.gsd.mybirawa.model.ModelChecklistInput;
import id.co.gsd.mybirawa.util.CustomSessionManager;
import id.co.gsd.mybirawa.util.SessionManager;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class ChecklistInputAdapter extends BaseAdapter {

    private CustomSessionManager dataSess;
    private Context context;
    private ChecklistSecondTabChildFragment fragmentTemp;
    private ModelChecklistInput model;
    private List<ModelChecklistInput> listModel;
    private SparseIntArray checkedState = new SparseIntArray();
    private String idPerangkatTab;
    private SessionManager session;
    private int index;

    public ChecklistInputAdapter(Context context, List<ModelChecklistInput> list, CustomSessionManager sess, String idPerangkatTab, ChecklistSecondTabChildFragment frag) {
        this.context = context;
        this.listModel = list;
        this.dataSess = sess;
        this.idPerangkatTab = idPerangkatTab;
        fragmentTemp = frag;
        this.session = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ModelChecklistInput getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getIsSeparator().equals("0")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        int type = getItemViewType(position);
        model = listModel.get(position);

        //if (view == null) {
        holder = new ViewHolder();
        switch (type) {
            case 0:
                view = inflater.inflate(R.layout.item_list_checklist_input_2, null);
                holder = new ViewHolder();

                holder.nama = view.findViewById(R.id.namaText);
                holder.standar = view.findViewById(R.id.standarText);
                holder.radioGroup = view.findViewById(R.id.radioGroup);
                holder.radioYes = view.findViewById(R.id.radioYes);
                holder.radioNo = view.findViewById(R.id.radioNo);
                holder.hasilUkur = view.findViewById(R.id.hasilUkurText);
                holder.hasilKamera = view.findViewById(R.id.kameraImage);
                holder.hasilKeterangan = view.findViewById(R.id.keteranganText);
                holder.textUkur = view.findViewById(R.id.hasilukur);
                holder.aSwitch = view.findViewById(R.id.btn_switch);
                break;
            case 1:
                view = inflater.inflate(R.layout.item_list_separator, null);
                holder = new ViewHolder();
                holder.separator = view.findViewById(R.id.separatorName);
                break;
        }
        view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }

        if (model.getIsSeparator().equals("1")) {
            holder.separator.setText(model.getDeskripsi());
        } else {
            String tipe = model.getChecklist_type();

            holder.nama.setText(model.getChecklist_name());
            String standarString = " Standard : " + model.getChecklist_std();
            holder.standar.setText(standarString);

            String kondisi = dataSess.getData("adaTidak" + position + idPerangkatTab);
            if (kondisi.equals("")) {
                holder.aSwitch.setChecked(true);
                dataSess.setData("adaTidak" + (position) + idPerangkatTab, "Ada");
            } else {
                if (kondisi.equals("Ada")) {
                    holder.aSwitch.setChecked(true);
                    holder.nama.setTextColor(Color.parseColor("#A71313"));
                    holder.standar.setTextColor(Color.parseColor("#000000"));
                    holder.textUkur.setTextColor(Color.parseColor("#000000"));
                    holder.hasilUkur.setTextColor(Color.parseColor("#000000"));
                    holder.hasilUkur.setEnabled(true);
                    holder.radioGroup.clearCheck();
                    holder.radioGroup.setEnabled(true);
                    holder.radioYes.setTextColor(Color.parseColor("#000000"));
                    holder.radioYes.setEnabled(true);
                    holder.radioNo.setTextColor(Color.parseColor("#000000"));
                    holder.radioNo.setEnabled(true);
                    holder.hasilKeterangan.setTextColor(Color.parseColor("#000000"));
                    holder.hasilKeterangan.setEnabled(true);
                    holder.hasilKamera.setImageResource(R.drawable.ic_camera);
                    holder.hasilKamera.setEnabled(true);
                } else {
                    holder.aSwitch.setChecked(false);
                    holder.nama.setTextColor(Color.GRAY);
                    holder.standar.setTextColor(Color.GRAY);
                    holder.hasilUkur.setTextColor(Color.GRAY);
                    holder.hasilUkur.setEnabled(false);
                    holder.radioGroup.clearCheck();
                    holder.radioGroup.setEnabled(false);
                    holder.radioYes.setTextColor(Color.GRAY);
                    holder.radioYes.setEnabled(false);
                    holder.radioNo.setTextColor(Color.GRAY);
                    holder.radioNo.setEnabled(false);
                    holder.hasilKeterangan.setTextColor(Color.GRAY);
                    holder.hasilKeterangan.setEnabled(false);
                    holder.hasilKamera.setImageResource(R.drawable.ic_camera_gray);
                    holder.hasilKamera.setEnabled(false);
                }
            }

            final ViewHolder finalHolder = holder;
            holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ada) {
                    if (ada) {
                        dataSess.setData("adaTidak" + (position) + idPerangkatTab, "Ada");
                        finalHolder.nama.setTextColor(Color.parseColor("#A71313"));
                        finalHolder.standar.setTextColor(Color.parseColor("#000000"));
                        finalHolder.textUkur.setTextColor(Color.parseColor("#000000"));
                        finalHolder.hasilUkur.setTextColor(Color.parseColor("#000000"));
                        finalHolder.hasilUkur.setEnabled(true);
                        finalHolder.radioGroup.clearCheck();
                        finalHolder.radioGroup.setEnabled(true);
                        finalHolder.radioYes.setTextColor(Color.parseColor("#000000"));
                        finalHolder.radioYes.setEnabled(true);
                        finalHolder.radioNo.setTextColor(Color.parseColor("#000000"));
                        finalHolder.radioNo.setEnabled(true);
                        finalHolder.hasilKeterangan.setTextColor(Color.parseColor("#000000"));
                        finalHolder.hasilKeterangan.setEnabled(true);
                        finalHolder.hasilKamera.setImageResource(R.drawable.ic_camera);
                        finalHolder.hasilKamera.setEnabled(true);
                    } else {
                        dataSess.setData("adaTidak" + (position) + idPerangkatTab, "Tidak Ada");
                        finalHolder.nama.setTextColor(Color.GRAY);
                        finalHolder.standar.setTextColor(Color.GRAY);
                        finalHolder.textUkur.setTextColor(Color.GRAY);
                        finalHolder.hasilUkur.setTextColor(Color.GRAY);
                        finalHolder.hasilUkur.setEnabled(false);
                        finalHolder.radioGroup.clearCheck();
                        finalHolder.radioGroup.setEnabled(false);
                        finalHolder.radioYes.setTextColor(Color.GRAY);
                        finalHolder.radioYes.setEnabled(false);
                        finalHolder.radioNo.setTextColor(Color.GRAY);
                        finalHolder.radioNo.setEnabled(false);
                        finalHolder.hasilKeterangan.setTextColor(Color.GRAY);
                        finalHolder.hasilKeterangan.setEnabled(false);
                        finalHolder.hasilKamera.setImageResource(R.drawable.ic_camera_gray);
                        finalHolder.hasilKamera.setEnabled(false);
                    }
                }
            });

            if (session.getRoleId().equals("5")) {
                holder.textUkur.setText("Kondisi Lapangan : ");
            } else {
                holder.textUkur.setText("Hasil Ukur : ");
            }

            if (tipe.equals("0")) {
                holder.radioGroup.setVisibility(View.VISIBLE);
                holder.hasilUkur.setVisibility(View.GONE);

                holder.radioGroup.setOnCheckedChangeListener(null);
                holder.radioGroup.setTag(position);

                String ukurs = dataSess.getData("hasilUkur" + position + idPerangkatTab);

                if (checkedState.indexOfKey(position) > -1 || !ukurs.equals("")) {
                    holder.radioGroup.check(checkedState.get(position));
                    if (ukurs.equals("Tidak")) {
                        holder.radioNo.setChecked(true);
                        holder.radioYes.setChecked(false);
                    } else {
                        holder.radioNo.setChecked(false);
                        holder.radioYes.setChecked(true);
                    }
                } else {
                    holder.radioGroup.clearCheck();
                }

                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        if (index > -1) {
                            checkedState.put(position, checkedId);
                            if (index == 0) {
                                dataSess.setData("hasilUkur" + (position) + idPerangkatTab, "Tidak");
                            } else {
                                dataSess.setData("hasilUkur" + (position) + idPerangkatTab, "Ya");
                            }
                        } else {
                            if (checkedState.indexOfKey(position) > -1)
                                checkedState.removeAt(checkedState.indexOfKey(position));
                        }
                    }
                });
            } else {
                holder.radioGroup.setVisibility(View.GONE);
                holder.hasilUkur.setVisibility(View.VISIBLE);

                String ukur = dataSess.getData("hasilUkur" + position + idPerangkatTab);
                if (ukur.equals("")) {
                    holder.hasilUkur.setText("");
                } else {
                    holder.hasilUkur.setText(ukur);
                }

                holder.hasilUkur.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        dataSess.setData("hasilUkur" + position + idPerangkatTab, charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }

            final String ket = dataSess.getData("hasilKeterangan" + position + idPerangkatTab);
            if (ket.equals("")) {
                holder.hasilKeterangan.setText("");
            } else {
                holder.hasilKeterangan.setText(ket);
            }

            holder.hasilKeterangan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    dataSess.setData("hasilKeterangan" + position + idPerangkatTab, charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            if (dataSess.getData("kamera" + position + idPerangkatTab).equals("")) {
                holder.hasilKamera.setImageResource(R.drawable.ic_camera);
            } else {
                byte[] decodedString = Base64.decode(dataSess.getData("kamera" + position + idPerangkatTab), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.hasilKamera.setImageBitmap(decodedByte);
            }

            holder.hasilKamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTemp.setCameraView(Integer.toString(position), 5);
                }
            });
        }

        return view;
    }

    static class ViewHolder {
        TextView nama;
        TextView standar;
        EditText hasilUkur;
        RadioGroup radioGroup;
        RadioButton radioYes;
        RadioButton radioNo;
        ImageView hasilKamera;
        EditText hasilKeterangan;
        TextView separator;
        TextView textUkur;
        Switch aSwitch;
    }
}