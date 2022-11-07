package ru.akirakozov.sd.refactoring.http;

import ru.akirakozov.sd.refactoring.dao.Product;

import java.util.List;

public class ResponseBuilder {
    final StringBuilder sb = new StringBuilder();
    String productResult;
    String numberResult;
    String arbitraryString;

    public ResponseBuilder setProductResult(final String header, final List<Product> products) {
        final StringBuilder productSb = new StringBuilder();
        if (header != null) {
            productSb.append("<h1>").append(header).append("</h1>\n");
        }
        for (final Product product : products) {
            productSb.append(product.getName()).append('\t').append(product.getPrice()).append("</br>\n");
        }
        this.productResult = productSb.toString();
        return this;
    }

    public ResponseBuilder setNumberResult(final String header, final int result) {
        this.numberResult = header + "\n" + result + "\n";
        return this;
    }

    public void setArbitraryString(final String arbitraryString) {
        this.arbitraryString = arbitraryString;
    }

    public String build() {
        sb.append("<html><body>\n");
        if (productResult != null) {
            sb.append(productResult);
        }
        if (numberResult != null) {
            sb.append(numberResult);
        }
        if (arbitraryString != null) {
            sb.append(arbitraryString);
        }
        sb.append("</body></html>");
        return sb.toString();
    }
}
