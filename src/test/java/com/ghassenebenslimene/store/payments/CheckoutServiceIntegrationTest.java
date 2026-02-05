package com.ghassenebenslimene.store.payments;

import com.ghassenebenslimene.store.auth.AuthService;
import com.ghassenebenslimene.store.carts.Cart;
import com.ghassenebenslimene.store.carts.CartItem;
import com.ghassenebenslimene.store.carts.CartRepository;
import com.ghassenebenslimene.store.orders.OrderRepository;
import com.ghassenebenslimene.store.products.Category;
import com.ghassenebenslimene.store.products.CategoryRepository;
import com.ghassenebenslimene.store.products.Product;
import com.ghassenebenslimene.store.products.ProductRepository;
import com.ghassenebenslimene.store.users.Role;
import com.ghassenebenslimene.store.users.User;
import com.ghassenebenslimene.store.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CheckoutServiceIntegrationTest {
    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private PaymentGateway paymentGateway;

    @MockitoBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void checkoutCreatesOrderAndClearsCart() {
        var user = new User();
        user.setName("Buyer");
        user.setEmail("buyer@example.com");
        user.setPassword("secret");
        user.setRole(Role.USER);
        userRepository.save(user);

        var category = new Category("Tools");
        categoryRepository.save(category);

        var product = new Product();
        product.setName("Hammer");
        product.setDescription("Steel hammer");
        product.setPrice(BigDecimal.valueOf(25));
        product.setCategory(category);
        productRepository.save(product);

        var cart = new Cart();
        cart = cartRepository.save(cart);

        var item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2);
        cart.getItems().add(item);

        cartRepository.save(cart);

        when(authService.getCurrentUser()).thenReturn(user);
        when(paymentGateway.createCheckoutSession(any()))
                .thenReturn(new CheckoutSession("http://checkout"));

        var request = new CheckoutRequest();
        request.setCartId(cart.getId());

        var response = checkoutService.checkout(request);

        assertThat(response.getOrderId()).isNotNull();
        assertThat(response.getCheckoutUrl()).isEqualTo("http://checkout");

        var savedCart = cartRepository.getCartWithItems(cart.getId()).orElseThrow();
        assertThat(savedCart.getItems()).isEmpty();

        var orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomer().getId()).isEqualTo(user.getId());
    }
}
