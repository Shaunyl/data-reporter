package it.shaunyl.datareporter.mainframe;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

/**
 *
 * @author Filippo Testino
 */
@RequiredArgsConstructor @ToString(exclude = { "description" })
public class PackCategory {

    private interface DefaultList {
        boolean add(final PackQuery packQuery);
        int size();
        PackQuery get(final int index);
    }
    
    @Getter @Setter
    private int id;
    
    @Getter @Setter
    private String name, description;

    @Getter @Delegate(types = DefaultList.class)
    private final List<PackQuery> queries = new ArrayList<>();
}
