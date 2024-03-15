package akki.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.google.protobuf.Duration;
import com.google.type.DateTime;

import spec.model.UserModel;

public class GrpcFactoryUtil {
    /**
     * {@link java.time.OffsetDateTime} を {@link com.google.type.DateTime} へ変換する
     *
     * @param offsetDateTime
     * @return UtcOffset が設定された DateTime
     */
    public static DateTime toDateTime(OffsetDateTime offsetDateTime) {
        return DateTime.newBuilder()
                .setYear(offsetDateTime.getYear())
                .setMonth(offsetDateTime.getMonthValue())
                .setDay(offsetDateTime.getDayOfMonth())
                .setHours(offsetDateTime.getHour())
                .setMinutes(offsetDateTime.getMinute())
                .setSeconds(offsetDateTime.getSecond())
                .setNanos(offsetDateTime.getNano())
                .setUtcOffset(Duration.newBuilder()
                        .setSeconds(offsetDateTime.getOffset().getTotalSeconds())
                        .build())
                .build();
    }

    /**
     * {@link java.time.LocalDateTime} を {@link java.util.TimeZone#getDefault()} 基準で<br>
     * {@link com.google.type.DateTime} へ変換する
     *
     * @param localDateTime
     * @return UtcOffset が設定された DateTime
     */
    public static DateTime toDateTime(LocalDateTime localDateTime) {
        return toDateTime(localDateTime, TimeZone.getDefault().toZoneId());
    }

    /**
     * {@link java.time.LocalDateTime} を zoneId 指定で {@link com.google.type.DateTime} へ変換する
     *
     * @param localDateTime
     * @param zoneId
     * @return UtcOffset が設定された DateTime
     */
    public static DateTime toDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        OffsetDateTime offsetDateTime = ZonedDateTime.of(localDateTime, zoneId).toOffsetDateTime();
        return toDateTime(offsetDateTime);
    }

    /**
     * {@link spec.model.UserModel}を生成する
     * @param createdUser
     * @param createTimestamp
     * @param updateUser
     * @param updateTimestamp
     * @return UserModel
     */
    public static UserModel createUserModels(String createdUser, LocalDateTime createTimestamp, String updateUser, LocalDateTime updateTimestamp) {
        var userModelBuilder = UserModel.newBuilder();
        if (StringUtils.isNotBlank(createdUser)) {
            userModelBuilder.setCreatedUser(createdUser);
        }
        if (createTimestamp != null) {
            userModelBuilder.setCreatedDatetime(toDateTime(createTimestamp));
        }
        if (StringUtils.isNotBlank(updateUser)) {
            userModelBuilder.setUpdatedUser(updateUser);
        }
        if (updateTimestamp != null) {
            userModelBuilder.setUpdatedDatetime(toDateTime(updateTimestamp));
        }

        return userModelBuilder.build();
    }
}
