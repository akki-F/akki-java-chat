package akki.model.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * メッセージ<br>
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class MessageEntity implements Serializable {
    /** メッセージ連番<br> bigint unsigned auto_increment Not Null (PK)  連番 */
    private long messageSeq;
    /** メッセージ内容<br> text Not Null   */
    private String messageContent;
    /** メッセージ投稿日時<br> datetime Not Null   */
    private java.time.LocalDateTime messageDatetime;
    /** メッセージ投稿者名称<br> varchar(255) Not Null   */
    private String picName;
    /** 作成日時<br> datetime    */
    private java.time.LocalDateTime createdDatetime;
    /** 作成ユーザーId<br> varchar(255)    */
    private String createdUser;
    /** 作成機能<br> varchar(255)    */
    private String createdFunc;
    /** 更新日時<br> datetime    */
    private java.time.LocalDateTime updatedDatetime;
    /** 更新ユーザーId<br> varchar(255)    */
    private String updatedUser;
    /** 更新機能<br> varchar(255)    */
    private String updatedFunc;
    /** 削除フラグ<br> tinyint(1) Not Null Default '0'  */
    private boolean deleteFlag;

    /**
     * メッセージ連番を取得する<br>
     * @return メッセージ連番
     */
    public long getMessageSeq() {
        return messageSeq;
    }

    /**
     * メッセージ連番を設定する<br>
     * @param messageSeq メッセージ連番
     */
    public void setMessageSeq(final long messageSeq) {
        this.messageSeq = messageSeq;
    }

    /**
     * メッセージ内容を取得する<br>
     * @return メッセージ内容
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * メッセージ内容を設定する<br>
     * @param messageContent メッセージ内容
     */
    public void setMessageContent(final String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * メッセージ投稿日時を取得する<br>
     * @return メッセージ投稿日時
     */
    public java.time.LocalDateTime getMessageDatetime() {
        return messageDatetime;
    }

    /**
     * メッセージ投稿日時を設定する<br>
     * @param messageDatetime メッセージ投稿日時
     */
    public void setMessageDatetime(final java.time.LocalDateTime messageDatetime) {
        this.messageDatetime = messageDatetime;
    }

    /**
     * メッセージ投稿者名称を取得する<br>
     * @return メッセージ投稿者名称
     */
    public String getPicName() {
        return picName;
    }

    /**
     * メッセージ投稿者名称を設定する<br>
     * @param picName メッセージ投稿者名称
     */
    public void setPicName(final String picName) {
        this.picName = picName;
    }

    /**
     * 作成日時を取得する<br>
     * @return 作成日時
     */
    public java.time.LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    /**
     * 作成日時を設定する<br>
     * @param createdDatetime 作成日時
     */
    public void setCreatedDatetime(final java.time.LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    /**
     * 作成ユーザーIdを取得する<br>
     * @return 作成ユーザーId
     */
    public String getCreatedUser() {
        return createdUser;
    }

    /**
     * 作成ユーザーIdを設定する<br>
     * @param createdUser 作成ユーザーId
     */
    public void setCreatedUser(final String createdUser) {
        this.createdUser = createdUser;
    }

    /**
     * 作成機能を取得する<br>
     * @return 作成機能
     */
    public String getCreatedFunc() {
        return createdFunc;
    }

    /**
     * 作成機能を設定する<br>
     * @param createdFunc 作成機能
     */
    public void setCreatedFunc(final String createdFunc) {
        this.createdFunc = createdFunc;
    }

    /**
     * 更新日時を取得する<br>
     * @return 更新日時
     */
    public java.time.LocalDateTime getUpdatedDatetime() {
        return updatedDatetime;
    }

    /**
     * 更新日時を設定する<br>
     * @param updatedDatetime 更新日時
     */
    public void setUpdatedDatetime(final java.time.LocalDateTime updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    /**
     * 更新ユーザーIdを取得する<br>
     * @return 更新ユーザーId
     */
    public String getUpdatedUser() {
        return updatedUser;
    }

    /**
     * 更新ユーザーIdを設定する<br>
     * @param updatedUser 更新ユーザーId
     */
    public void setUpdatedUser(final String updatedUser) {
        this.updatedUser = updatedUser;
    }

    /**
     * 更新機能を取得する<br>
     * @return 更新機能
     */
    public String getUpdatedFunc() {
        return updatedFunc;
    }

    /**
     * 更新機能を設定する<br>
     * @param updatedFunc 更新機能
     */
    public void setUpdatedFunc(final String updatedFunc) {
        this.updatedFunc = updatedFunc;
    }

    /**
     * 削除フラグを取得する<br>
     * @return 削除フラグ
     */
    public boolean getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 削除フラグを設定する<br>
     * @param deleteFlag 削除フラグ
     */
    public void setDeleteFlag(final boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


}
