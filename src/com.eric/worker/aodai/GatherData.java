package com.eric.worker.aodai;

import com.eric.util.HttpClientUtils;
import com.eric.util.Tool;
import com.eric.worker.aodai.bean.Product;
import com.eric.worker.aodai.bean.Result;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class GatherData {

    private String productId;
    private final String imagePath = "D://tmp/aodai/";
    private final String cloud_url = "http://public-assets-1252263591.file.myqcloud.com/";

    private Gson gson = new Gson();

    public void gatherData() {
        String result = HttpClientUtils.doGet("http://www.lanney.com.au/gs/product/saleStock?defaultCurrency=RMB&productId=" + productId + "&v2=true");
        if (StringUtils.isNotBlank(result)) {
            Result object = gson.fromJson(result, Result.class);
            Product product = object.getData().getProduct();
            System.out.println(object.getData().getProduct());

            //下载图片
            File proFile = Tool.mkdirs(imagePath + productId);
            if (product != null && product.getImages() != null) {
                int i = 1;
                for (String url : product.getImages()) {
                    url = url.replace("_96", "");
                    HttpClientUtils.downloadPicture(cloud_url + url, proFile.getAbsolutePath(), i + "_" + url.substring(url.lastIndexOf("/") + 1));
                    ++i;
                }
            }
            System.out.println(productId + "抓取成功...\n");
        }
    }

    public GatherData(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
