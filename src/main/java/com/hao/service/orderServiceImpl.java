package com.hao.service;

import com.hao.mapper.orderMapper;
import com.hao.mapper.riderUserMapper;
import com.hao.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class orderServiceImpl implements orderService{
    @Autowired
    private orderMapper mapper;
    @Autowired
    private riderUserMapper ridermapper;


    public List<Order> getAllOrder(String openid) {
        List<Order> orders = mapper.getAllOrders(openid);
        return orders;

    }

    @Override
    public List<Order> getOrderByRider(String rider) {
        List<Order> orders = mapper.getOrdersByRider(rider);
        return orders;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = mapper.getAll();
        return orders;
    }

    @Transactional(readOnly = false)
    public int addOrder(Order order) {
        int i = mapper.addOrder(order);
        return i;
    }

    @Transactional(readOnly = false)
    public int completeOrder(int id) {
        int i = mapper.ChangeStatus(id,1);
        return i;
    }

    @Override
    public int cancelOrder(int id) {
        int i = mapper.ChangeStatus(id,2);
        return i;
    }

    @Override
    public int refoundOrder(int id) {
        int i = mapper.ChangeStatus(id,3);
        return i;
    }

    @Override
    @Transactional(readOnly = false)
    public int RiderCancel(String order_id, String rider, String orderNum) {
        int i = mapper.RiderCancel("n",order_id);
        int j = ridermapper.changeOrder(orderNum,rider);
        return i*j;
    }

    @Override
    @Transactional(readOnly = false)
    public int DoOrder(String order_id,String rider,String orderNum) {

        int i = mapper.TodoOrder(rider, order_id);
        int j = ridermapper.changeOrder(orderNum,rider);
        return i*j;
    }

    @Override
    public int deleteOrder(String order_id) {
        int i = mapper.deleteOrder(order_id);
        return i;
    }
}
