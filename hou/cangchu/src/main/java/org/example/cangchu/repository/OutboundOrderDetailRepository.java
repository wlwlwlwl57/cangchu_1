package org.example.cangchu.repository;

import org.example.cangchu.entity.OutboundOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 出库单明细数据访问接口
 * 提供出库单明细数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface OutboundOrderDetailRepository extends JpaRepository<OutboundOrderDetail, Long> {

    /**
     * 根据出库单ID查询明细列表
     * 
     * @param orderId 出库单ID
     * @return 明细列表
     */
    List<OutboundOrderDetail> findByOrderId(Long orderId);
}
