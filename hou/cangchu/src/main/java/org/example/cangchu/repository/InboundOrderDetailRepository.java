package org.example.cangchu.repository;

import org.example.cangchu.entity.InboundOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 入库单明细数据访问接口
 * 提供入库单明细数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface InboundOrderDetailRepository extends JpaRepository<InboundOrderDetail, Long> {

    /**
     * 根据入库单ID查询明细列表
     * 
     * @param orderId 入库单ID
     * @return 明细列表
     */
    List<InboundOrderDetail> findByOrderId(Long orderId);
}
