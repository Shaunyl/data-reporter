package it.shaunyl.datareporter.mainframe;

import lombok.*;

/**
 *
 * @author Filippo Testino
 */
@RequiredArgsConstructor @ToString(exclude = { "description" })
public class PackQuery {

    @Getter @Setter
    private int id;    
    
    @Getter @Setter
    private String category, sql, description;
}
