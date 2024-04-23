package com.example.cleaningapplication.Model;

public class    Products {


    private String beforeImage,afterImage,requester,requestdate,category, date, image, lister_shopName, pid, product_details, product_name, product_location,
            product_price, search, sellers_contact, time, userId, status, remarks, request_worker, request_worker1, request_worker2, request_worker3,chosenWorker;

    private int workerIsDone;
    private boolean isRequestAvailable,isWorkerAvailable;

    public Products() {

    }

    public Products(int workerIsDone,String beforeImage,String afterImage,String requester,String requestdate,String category, String remarks, String status, String date, String image, String lister_shopName, String pid, String product_details, String product_name, String product_location, String product_price, String search, String sellers_contact, String time, String userId, Boolean isRequestAvailable, String request_worker, String request_worker1, String request_worker2, String request_worker3,boolean isWorkerAvailable, String chosen_worker) {
        this.category = category;
        this.date = date;
        this.workerIsDone = workerIsDone;
        this.beforeImage = beforeImage;
        this.afterImage = afterImage;
        this.requester = requester;
        this.requestdate = requestdate;
        this.image = image;
        this.lister_shopName = lister_shopName;
        this.pid = pid;
        this.isWorkerAvailable = isWorkerAvailable;
        this.product_details = product_details;
        this.product_name = product_name;
        this.product_location = product_location;
        this.product_price = product_price;
        this.search = search;
        this.sellers_contact = sellers_contact;
        this.time = time;
        this.userId = userId;
        this.status = status;
        this.remarks = remarks;
        this.isRequestAvailable = isRequestAvailable;
        this.chosenWorker = chosen_worker;
        this.request_worker = request_worker;
        this.request_worker1 = request_worker1;
    }




    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public int getWorkerIsDone() {
        return workerIsDone;
    }

    public void setWorkerIsDone(int workerIsDone) {
        this.workerIsDone = workerIsDone;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public void setBeforeImage(String beforeImage) {
        this.beforeImage = beforeImage;
    }

    public String getAfterImage() {
        return afterImage;
    }

    public void setAfterImage(String afterImage) {
        this.afterImage = afterImage;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }
    // worker of request

    public String getChosenWorker() {
        return chosenWorker;
    }

    public void setChosenWorker(String chosenWorker) {
        this.chosenWorker = chosenWorker;
    }

    public String getRequest_worker() {
        return request_worker;
    }

    public void setRequest_worker(String request_worker) {
        this.request_worker = request_worker;
    }

    public String getRequest_worker1() {
        return request_worker1;
    }

    public void setRequest_worker1(String request_worker1) {
        this.request_worker1 = request_worker1;
    }


    public Boolean isRequestAvailable() {
        return isRequestAvailable;
    }

    public void setRequestAvailable(Boolean isRequestAvailable) {
        this.isRequestAvailable = isRequestAvailable;
    }

    public Boolean isWorkerAvailable() {
        return isWorkerAvailable;
    }

    public void setWorkerAvailable(Boolean isWorkerAvailable) {
        this.isWorkerAvailable = isWorkerAvailable;
    }


    //


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLister_shopName() {
        return lister_shopName;
    }

    public void setLister_shopName(String lister_shopName) {
        this.lister_shopName = lister_shopName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_location() {
        return product_location;
    }

    public void setProduct_location(String product_location) {
        this.product_location = product_location;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSellers_contact() {
        return sellers_contact;
    }

    public void setSellers_contact(String sellers_contact) {
        this.sellers_contact = sellers_contact;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
