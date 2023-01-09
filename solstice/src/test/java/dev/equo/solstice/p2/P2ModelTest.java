package dev.equo.solstice.p2;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class P2ModelTest {
    @Test
    public void toString(Expect expect) {
        P2Model model = new P2Model();
        StringBuilder buf = new StringBuilder();
        addToStringTest(buf, "EMPTY", model);

        model.getP2repo().add("https://download.eclipse.org/eclipse/updates/4.26/");
        addToStringTest(buf, "P2 SINGLE", model);

        model.getP2repo().add("https://download.eclipse.org/buildship/updates/e423/releases/3.x/3.1.6.v20220511-1359/");
        addToStringTest(buf, "P2 MULTIPLE", model);

        model.getInstall().add("org.eclipse.platform.ide.categoryIU");
        addToStringTest(buf, "INSTALL SINGLE", model);

        model.getInstall().add("org.eclipse.releng.java.languages.categoryIU");
        model.getInstall().add("org.eclipse.buildship.feature.group");
        addToStringTest(buf, "INSTALL MULTIPLE", model);

        expect.toMatchSnapshot(buf.toString().trim());
    }

    private void addToStringTest(StringBuilder buf, String name, P2Model model) {
        buf.append(name);
        buf.append('\n');
        buf.append(model.toString());
        buf.append('\n');
    }
}
