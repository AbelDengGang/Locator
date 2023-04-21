package com.dr.libloc.adv;

import com.dr.libloc.attribute.MapItem;
import com.dr.libloc.attribute.PositionPost;
import com.dr.libloc.interfaces.AdvEventListener;
import com.dr.libloc.interfaces.MapEventListener;
import com.dr.libloc.locator.Locator;
import com.dr.libloc.locator.LocatorMgr;
import com.dr.libloc.locator.PositionPostUpdateListener;
import com.dr.libloc.mapUtil.DRMap;

import java.util.ArrayList;
import java.util.List;

public class AdvMgr implements AdvEventListener, MapEventListener, PositionPostUpdateListener {
    private String memberName;
    private DRMap drMap;
    private List<String> listAdv;
    private Locator locator;


    public AdvMgr() {
        listAdv = new ArrayList<>();
        locator = LocatorMgr.getLocator();
    }

    @Override
    public void onStart() {
        locator.regPPUpdateListener(this);
    }

    @Override
    public void onStop() {
        locator.rmPPUpdateListener(this);
    }

    @Override
    public void onItemClicked() {

    }

    public void mapItemToAdvItem(){

    }

    public MapItem getProximalMapItems(){

        return null;
    }

    @Override
    public void onPPUpdate(Locator locator, PositionPost positionPost) {

    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public DRMap getDrMap() {
        return drMap;
    }

    public void setDrMap(DRMap drMap) {
        this.drMap = drMap;
    }

    public List<String> getListAdv() {
        return listAdv;
    }

    public void setListAdv(List<String> listAdv) {
        this.listAdv = listAdv;
    }

}
