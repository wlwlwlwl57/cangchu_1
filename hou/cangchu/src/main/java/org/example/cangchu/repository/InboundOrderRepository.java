package org.example.cangchu.repository;

import org.example.cangchu.entity.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 入库单数据访问接口
 * 提供入库单数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface InboundOrderRepository extends JpaRepository<InboundOrder, Long> {

    /**
     * 根据单号前缀统计入库单数量
     * 用于生成连续的单号
     * 
     * @param prefix 单号前缀
     * @return 数量
     */
    @Query("SELECT COUNT(o) FROM InboundOrder o WHERE o.orderNo LIKE CONCAT(:prefix, '%')")
    long countByOrderNoPrefix(@Param("prefix") String prefix);
}
