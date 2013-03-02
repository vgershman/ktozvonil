package com.vgershman.whocall.connection;

import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */
public interface RequestGetCallback {

    void onInfoFound(PhoneUserInfo response);
    void onNotFound(NotFoundInfo response);
    void onFailure();


}
