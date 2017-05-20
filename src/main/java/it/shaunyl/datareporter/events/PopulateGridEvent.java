package it.shaunyl.datareporter.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Filippo Testino
 */
@RequiredArgsConstructor
public class PopulateGridEvent {
    
    @Getter
    private final String sql;
}
