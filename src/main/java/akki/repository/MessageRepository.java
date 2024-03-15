package akki.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import akki.model.behavior.MessageSearcher;
import akki.model.entity.MessageEntity;


/**
 * メッセージRepository
 */
@Mapper
public interface MessageRepository {
    /**
     * messageを取得する
     * @param param
     * @return List<MessageEntity>
     */
    List<MessageEntity> selectMessage(@Param("param") MessageSearcher param);

    /**
     * メッセージ保存・更新処理
     * 
     * @param param
     */
    @Insert("""
            <script>
            INSERT INTO message(
                message_seq
                , message_content
                , message_datetime
                , pic_name
                , created_datetime
                , created_user
                , created_func
                , updated_datetime
                , updated_user
                , updated_func
                , delete_flag
            )
            VALUES
            (
                #{param.messageSeq}
                , #{param.messageContent}
                , NOW()
                , #{param.picName}
                , NOW()
                , #{param.createdUser}
                , #{param.createdFunc}
                , NOW()
                , #{param.updatedUser}
                , #{param.updatedFunc}
                , #{param.deleteFlag}
            )
            AS NEW
            ON DUPLICATE KEY UPDATE
                message_content = NEW.message_content
                , updated_datetime = NOW()
                , updated_user = NEW.updated_user
                , updated_func = NEW.updated_func
                , delete_flag = NEW.delete_flag
            </script>
            """)
    void upsertMessage(@Param("param") MessageEntity param);
}
