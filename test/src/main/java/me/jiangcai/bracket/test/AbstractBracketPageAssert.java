package me.jiangcai.bracket.test;

import me.jiangcai.lib.test.page.AbstractPageAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;

import java.util.List;

/**
 * @author CJ
 * @since 1.1
 */
public class AbstractBracketPageAssert<S extends AbstractBracketPageAssert<S, A>, A extends BracketPage>
        extends AbstractPageAssert<S, A> {

    // 2 - Write a constructor to build your assertion class with the object you want make assertions on.
    protected AbstractBracketPageAssert(A actual, Class<?> selfType) {
        super(actual, selfType);
    }

    // 3 - A fluent entry point to your specific assertion class, use it with static import.
    public static AbstractBracketPageAssert<?, ? extends BracketPage> assertThat(BracketPage actual) {
        return new BracketPageAssert(actual);
    }

    public AbstractListAssert<?, ? extends List<? extends String>, String> gritterMessage(String type) {
        return Assertions.assertThat(actual.getGritterMessage(type));
    }

    // 4 - a specific assertion !
    public AbstractListAssert<?, ? extends List<? extends String>, String> gritterDangerMessage() {
        return gritterMessage("growl-danger");
    }


}
