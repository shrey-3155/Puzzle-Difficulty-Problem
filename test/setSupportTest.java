import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class setSupportTest {

    @Test
    void okSupport() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertTrue( library.setSupport( 5 ), "Set support at 5%" );
    }
}