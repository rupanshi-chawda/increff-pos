package com.increff.pos.api;

import com.increff.pos.helper.OrderHelper;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderApi {
    
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao itemDao;
    
    public void addOrder(OrderPojo p) throws ApiException {
        orderDao.insert(p);
    }
    
    public OrderPojo getOrder(int id) throws ApiException {
        return getOrderId(id);
    }

    public List<OrderPojo> getAllOrder() {
        return orderDao.selectAll(OrderPojo.class);
    }
    
    public void addItem(OrderItemPojo p, int pid, int oid) throws ApiException {
        p.setProductId(pid);
        p.setOrderId(oid);
        itemDao.insert(p);
    }

    public OrderItemPojo getItem(int id) throws ApiException {
        OrderItemPojo p = getOrderItemId(id);
        OrderHelper.validateId(p, id);
        return p;
    }

    public List<OrderItemPojo> getAllItem() {
        return itemDao.selectAll(OrderItemPojo.class);
    }

    public List<OrderItemPojo> getByOrderId(int orderid) {
        return itemDao.selectByOrderId(orderid);
    }

    private OrderItemPojo getOrderItemId(int id) {
        return itemDao.selectById(id, OrderItemPojo.class);
    }

    private OrderPojo getOrderId(int id) throws ApiException {
        OrderPojo p = orderDao.selectById(id, OrderPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Order with given ID does not exit, id: " + id);
        }
        return p;
    }

    public List<OrderItemPojo> getOrderItemByOrderItem(int orderid) throws ApiException {
        return itemDao.selectByOrderId(orderid);
    }

    public ResponseEntity<byte[]> getPDF(InvoiceForm invoiceForm) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8085/fop/api/invoice";

        byte[] contents = restTemplate.postForEntity(url, invoiceForm, byte[].class).getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "invoice.pdf";
        //headers.setContentDispositionFormData("inline", filename);
        headers.add("Content-Disposition", "inline;filename=" + filename);


        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }
}
