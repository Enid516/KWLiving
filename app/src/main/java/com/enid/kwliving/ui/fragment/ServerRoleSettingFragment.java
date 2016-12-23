package com.enid.kwliving.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.enid.kwliving.R;
import com.enid.kwliving.model.RoleEnum;
import com.enid.kwliving.model.RoleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big_love on 2016/12/21.
 */

public class ServerRoleSettingFragment extends BaseFragment{
    private List<RoleModel> roleList = new ArrayList<>();
    private EditText etParticipantNumber,etWerewolvesNumber,etVillagerNumber;
    private CheckBox checkBoxSeer,checkBoxThief,checkBoxHunter,checkBoxCupid,checkBoxWitch,checkBoxLittleGirl,checkBoxSheriff;
    private Button buttonStart;
    private boolean isStart = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_server_role_setting,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){
        etParticipantNumber  =  (EditText) view.findViewById(R.id.etParticipantNumber);
        etWerewolvesNumber  =  (EditText) view.findViewById(R.id.etWerewolvesNumber);
        etVillagerNumber  =  (EditText) view.findViewById(R.id.etVillagerNumber);
        checkBoxSeer  =  (CheckBox) view.findViewById(R.id.checkBoxSeer);
        checkBoxThief  =  (CheckBox) view.findViewById(R.id.checkBoxThief);
        checkBoxHunter  =  (CheckBox) view.findViewById(R.id.checkBoxHunter);
        checkBoxCupid  =  (CheckBox) view.findViewById(R.id.checkBoxCupid);
        checkBoxWitch  =  (CheckBox) view.findViewById(R.id.checkBoxWitch);
        checkBoxLittleGirl  =  (CheckBox) view.findViewById(R.id.checkBoxLittleGirl);
        checkBoxSheriff  =  (CheckBox) view.findViewById(R.id.checkBoxSheriff);
        buttonStart  =  (Button) view.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    getRoleList();
                    isStart = true;
                }else {
                    isStart = false;
                }
                updateRolesettingChanging(!isStart);
            }
        });
        checkBoxSeer.setChecked(true);
    }

    private void updateRolesettingChanging(boolean canUse){
        etParticipantNumber.setFocusable(canUse);
        etWerewolvesNumber.setFocusable(canUse);
        etVillagerNumber.setFocusable(canUse);
        checkBoxSeer.setClickable(canUse);
        checkBoxThief.setClickable(canUse);
        checkBoxHunter.setClickable(canUse);
        checkBoxCupid.setClickable(canUse);
        checkBoxWitch.setClickable(canUse);
        checkBoxLittleGirl.setClickable(canUse);
        checkBoxSheriff.setClickable(canUse);
    }


    private void getRoleList(){
        int participantNumber = Integer.valueOf(etParticipantNumber.getText().toString());
        int werewolvesNumber = Integer.valueOf(etWerewolvesNumber.getText().toString());
        int villagerNumber = Integer.valueOf(etVillagerNumber.getText().toString());


        //getRole
        RoleModel roleModel;
        roleList.clear();
        for (int i = 0 ; i < werewolvesNumber;i++) {
            roleModel = new RoleModel(RoleEnum.WEREWOLVES,getResources().getString(R.string.role_werewolves));
            roleList.add(roleModel);
        }
        for (int i = 0 ; i < villagerNumber;i++) {
            roleModel = new RoleModel(RoleEnum.VILLAGER,getResources().getString(R.string.role_villager));
            roleList.add(roleModel);
        }
        if (checkBoxSeer.isChecked()) {
            roleModel = new RoleModel(RoleEnum.SEER,getResources().getString(R.string.role_seer));
            roleList.add(roleModel);
        }else{
            //TODO 提示checkBoxSeer 必须选择
        }
        if (checkBoxThief.isChecked()) {
            roleModel = new RoleModel(RoleEnum.THIEF,getResources().getString(R.string.role_thief));
            roleList.add(roleModel);
        }
        if (checkBoxHunter.isChecked()) {
            roleModel = new RoleModel(RoleEnum.HUNTER,getResources().getString(R.string.role_hunter));
            roleList.add(roleModel);
        }
        if (checkBoxCupid.isChecked()) {
            roleModel = new RoleModel(RoleEnum.CUPIDO,getResources().getString(R.string.role_cupid));
            roleList.add(roleModel);
        }
        if (checkBoxWitch.isChecked()) {
            roleModel = new RoleModel(RoleEnum.WITCH,getResources().getString(R.string.role_witch));
            roleList.add(roleModel);
        }
        if (checkBoxLittleGirl.isChecked()) {
            roleModel = new RoleModel(RoleEnum.LITTLE_GIRL,getResources().getString(R.string.role_little_girl));
            roleList.add(roleModel);
        }
        if (checkBoxSheriff.isChecked()) {
            roleModel = new RoleModel(RoleEnum.SHERIFF,getResources().getString(R.string.role_sheriff));
            roleList.add(roleModel);
        }
    }
}
