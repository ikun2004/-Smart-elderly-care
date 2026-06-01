package cn.yangeit.mapper;


import cn.yangeit.pojo.Reservation;
import cn.yangeit.vo.TimeCountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Entity cn.yangeit.pojo.Reservation
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {

    List<TimeCountVo> countReservationsForTime(@Param("startTime")LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
}




