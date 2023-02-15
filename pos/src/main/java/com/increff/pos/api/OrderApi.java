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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    
    public void addOrder(OrderPojo p) {
        orderDao.insert(p);
    }

    public void addOrder(OrderPojo p, List<OrderItemPojo> items) {
        orderDao.insert(p);

    }

    public OrderPojo getOrder(Integer id) throws ApiException {
        return getCheckByOrderId(id);
    }

    public List<OrderPojo> getAllOrder() {
        return orderDao.selectAllDesc();
    }
    
    public void addItem(OrderItemPojo p, Integer pid, Integer oid){
        p.setProductId(pid);
        p.setOrderId(oid);
        itemDao.insert(p);
    }

//    public OrderItemPojo getItem(Integer id) throws ApiException {
//        OrderItemPojo p = getOrderItemId(id);
//        OrderHelper.validateId(id);
//        return p;
//    }


    public List<OrderItemPojo> getByOrderId(Integer orderId) {
        return itemDao.selectByOrderId(orderId);
    }

//    private OrderItemPojo getOrderItemId(Integer id) {
//        return itemDao.selectById(id, OrderItemPojo.class);
//    }

    private OrderPojo getCheckByOrderId(Integer id) throws ApiException {
        OrderPojo p = orderDao.selectById(id, OrderPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Order with given ID does not exists, id: " + id);
        }
        return p;
    }

    public List<OrderItemPojo> getOrderItemByOrderId(Integer orderId){
        return itemDao.selectByOrderId(orderId);
    }

    public void update(Integer id, OrderPojo p) throws ApiException {
        OrderPojo b = getOrder(id);
        b.setInvoicePath(p.getInvoicePath());
        orderDao.update(p);
    }

    // Business Logic Methods

    public ResponseEntity<byte[]> getPDF(InvoiceForm invoiceForm) throws ApiException {

        Integer orderId = invoiceForm.getOrderId();
        String PDF_PATH = "C:\\Users\\KIIT\\Downloads\\increff-pos\\pos\\src\\main\\resources\\invoices\\";
        String _filename = PDF_PATH + "invoice_" + orderId + ".pdf";
        File f = new File(_filename);
        byte[] contents;
        if(f.exists())
        {
            Path pdfPath = Paths.get(_filename);
            try {
                contents = Files.readAllBytes(pdfPath);
            } catch (IOException e) {
                throw new ApiException("Unable to read bytes from pdf" + e.getMessage());
            }
        }
        else
        {
            String base64 = restTemplate.postForObject(url, invoiceForm, String.class);
            Path pdfPath = Paths.get(_filename);

            contents = Base64.getDecoder().decode(base64);

            try {
                Files.write(pdfPath, contents);
            } catch (IOException e) {
                throw new ApiException("Unable to create PDF in pos" + e.getMessage());
            }
        }

        OrderPojo p = getOrder(orderId);
        p.setInvoicePath(_filename);
        update(orderId, p);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "invoice_" + orderId + ".pdf";
        headers.add("Content-Disposition", "inline");
        headers.add("filename",filename);

        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    public List<OrderPojo> getOrderByDateFilter(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderDao.selectOrderByDateFilter(startDate,endDate);
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Integer id) {
        return itemDao.selectByOrderId(id);
    }
}
