package gui.dashboard.model;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<CartItem> items;
    private Runnable onCartChanged;

    public CartManager() {
        items = new ArrayList<>();
    }

    public void setOnCartChanged(Runnable listener) {
        this.onCartChanged = listener;
    }

    public void addProduct(Product p, int qty) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(p.getId())) {
                item.setQuantity(item.getQuantity() + qty);
                notifyChanged();
                return;
            }
        }
        items.add(new CartItem(p, qty));
        notifyChanged();
    }

    public void updateQuantity(String productId, int qty) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(qty);
                break;
            }
        }
        notifyChanged();
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        notifyChanged();
    }

    public void clearCart() {
        items.clear();
        notifyChanged();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(CartItem::getTotal).sum();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    private void notifyChanged() {
        if (onCartChanged != null) {
            onCartChanged.run();
        }
    }
}
