package com.vgershman.whocall.connection;

import com.vgershman.whocall.dto.Comment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 26.02.13
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public interface CommentCallback {

    void onFound(List<Comment> commentList);
    void onNotFound();
    void onError();
}
