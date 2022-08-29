package com.jerry.tvplayer.m3u;

import android.text.TextUtils;

/**
 * Data class for M3U Entries with getters & setters
 */
public class M3U_Entry {
    private String _tvgName, _name;
    private String _tvgLogo;
    private String _tvgEpgUrl;
    private String _tvgId;
    private String status;
    private String _groupTitle;
    private String _url;
    private String[] _tags = new String[0];
    private int _seconds = -1;
    private boolean _isRadio = false;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTvgName(String value) {
        _tvgName = value;
    }

    public String getName() {
        return _tvgName != null ? _tvgName : _name;
    }

    public void setName(String value) {
        _name = value;
    }

    public String getTvgLogo() {
        return _tvgLogo;
    }

    public void setTvgLogo(String value) {
        _tvgLogo = value;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String value) {
        _url = value;
    }

    public int getSeconds() {
        return _seconds;
    }

    public void setSeconds(int value) {
        _seconds = value;
    }

    public String getTvgEpgUrl() {
        return _tvgEpgUrl;
    }

    public void setTvgEpgUrl(String value) {
        _tvgEpgUrl = value;
    }

    public boolean isRadio() {
        return _isRadio;
    }

    public String getTvgId() {
        return TextUtils.isEmpty(_tvgId) ? _name : _tvgId;
    }

    public void setTvgId(String value) {
        _tvgId = value;
    }

    public String getGroupTitle() {
        return _groupTitle;
    }

    public void setGroupTitle(String value) {
        _groupTitle = value;
    }

    public void setIsRadio(boolean value) {
        _isRadio = value;
    }

    public String[] getTags() {
        return _tags;
    }

    public void setTags(String[] value) {
        _tags = value;
    }

    @Override
    public String toString() {
        return getName() + " " + getUrl();
    }
}
