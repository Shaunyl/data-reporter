package it.shaunyl.presentationmodel.role;

import java.util.List;
import javax.annotation.Nonnull;

public interface GridHeaderProvider {

    @Nonnull
    public List<String> getHeaderLabels();
}