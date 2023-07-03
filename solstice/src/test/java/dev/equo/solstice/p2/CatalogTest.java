package dev.equo.solstice.p2;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import dev.equo.ide.Catalog;
import dev.equo.ide.CatalogDsl;
import dev.equo.ide.IdeHook;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class CatalogTest {
    @Test
    public void equoChatGptDefault(Expect expect) {
        expect.toMatchSnapshot(requestModel(new TestCatalogDsl(Catalog.CHATGPT, null)));
    }

    @Test
    public void equoChatGptFixed(Expect expect) {
        expect.toMatchSnapshot(requestModel(new TestCatalogDsl(Catalog.CHATGPT, "1.0.0")));
    }

    static class TestCatalogDsl extends CatalogDsl {
        public TestCatalogDsl(Catalog catalog, String urlOverride) {
            super(catalog);
            super.setUrlOverride(urlOverride);
        }
    }

    private String requestModel(TestCatalogDsl... catalogs) {
        var list = new CatalogDsl.TransitiveAwareList<TestCatalogDsl>();
        for (var catalog : catalogs) {
            list.add(catalog);
        }
        var model = new P2Model();
        var hooks = new IdeHook.List();
        list.putInto(model, hooks);

        return ConsoleTable.request(model, ConsoleTable.Format.ascii);
    }
}
