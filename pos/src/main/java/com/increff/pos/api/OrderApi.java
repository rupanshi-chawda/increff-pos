package com.increff.pos.api;

import com.increff.pos.helper.OrderHelper;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderApi {
    
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao itemDao;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${invoice.url}")
    private String url;
    
    public void addOrder(OrderPojo p){//TODO read about checked and unchecked exception
        orderDao.insert(p);
    }
    
    public OrderPojo getOrder(int id) throws ApiException {
        return getCheckByOrderId(id);
    }

    public List<OrderPojo> getAllOrder() {
        return orderDao.selectAllDesc();
    }
    
    public void addItem(OrderItemPojo p, int pid, int oid){
        p.setProductId(pid);
        p.setOrderId(oid);
        itemDao.insert(p);
    }

    public OrderItemPojo getItem(int id) throws ApiException {
        OrderItemPojo p = getOrderItemId(id);
        OrderHelper.validateId(p, id);
        return p;
    }


    public List<OrderItemPojo> getByOrderId(int orderId) {
        return itemDao.selectByOrderId(orderId);
    }

    private OrderItemPojo getOrderItemId(int id) {
        return itemDao.selectById(id, OrderItemPojo.class);
    }

    private OrderPojo getCheckByOrderId(int id) throws ApiException {
        OrderPojo p = orderDao.selectById(id, OrderPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Order with given ID does not exit, id: " + id);
        }
        return p;
    }

    public List<OrderItemPojo> getOrderItemByOrderId(int orderId){
        return itemDao.selectByOrderId(orderId);
    }

    public ResponseEntity<byte[]> getPDF(InvoiceForm invoiceForm) {

        String base64 = restTemplate.postForObject(url, invoiceForm, String.class);
        //Path pdfPath = Paths.get(PDF_PATH +id+"invoice.pdf");
        byte[] contents = Base64.getDecoder().decode(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "invoice.pdf";//TODO append orderId
        headers.add("Content-Disposition", "inline;filename=" + filename);


        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    public List<OrderPojo> getOrderByDateFilter(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderDao.selectOrderByDateFilter(startDate,endDate);
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(int id) {
        return itemDao.selectByOrderId(id);
    }
}
