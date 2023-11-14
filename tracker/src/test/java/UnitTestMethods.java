import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import app.db.*;

class UnitTestMethods {

    static Database stubDB = new Database("DB");
    static Food stubFoodWithDisplayName = new Food(stubDB, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    static Food stubFoodWithNullDisplayName = new Food(stubDB, "Full Name2", null, 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");

    protected static Stream<Arguments> providesFoodObjectsWithNames() {
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "Full Name1"),
        Arguments.of(stubFoodWithNullDisplayName, "Full Name2")
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithDisplayNames() {
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "Display Name"),
        Arguments.of(stubFoodWithNullDisplayName, "Full Name2")
        );
    }
}