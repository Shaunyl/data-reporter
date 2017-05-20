package it.shaunyl.presentationmodel.role.spi;

import it.shaunyl.presentationmodel.role.Describable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Filippo
 */
@RequiredArgsConstructor
public class DefaultDescribable implements Describable {
    @Getter
    public final String description;
}
