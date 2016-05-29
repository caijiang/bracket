package me.jiangcai.lib.bracket.thymeleaf.process;

import me.jiangcai.lib.bracket.thymeleaf.BracketDialect;
import me.jiangcai.lib.bracket.thymeleaf.BracketProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinition;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.standard.util.StandardProcessorUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.Validate;

import static me.jiangcai.lib.bracket.thymeleaf.process.ClassesByActiveMenuProcessor.MENU_ACTIVED;

/**
 * Menu Children,
 * 如果是子菜单,上级菜单不在active的状态下 应当先隐藏。
 * 它的值没有任何意义。
 *
 * @author Jolene
 */
@Component
public class MenuChildrenProcessor extends AbstractStandardExpressionAttributeTagProcessor
        implements BracketProcessor, IAttributeDefinitionsAware {

    public static final int ATTR_PRECEDENCE = 21100;
    private static final Log log = LogFactory.getLog(MenuChildrenProcessor.class);
    private AttributeDefinition targetAttributeDefinition;

    protected MenuChildrenProcessor() {
        super(TemplateMode.HTML, BracketDialect.PREFIX, "menuChildren", ATTR_PRECEDENCE, false);
    }

    public void setAttributeDefinitions(final AttributeDefinitions attributeDefinitions) {
        Validate.notNull(attributeDefinitions, "Attribute Definitions cannot be null");
        // We precompute the AttributeDefinition of the target attribute in order to being able to use much
        // faster methods for setting/replacing attributes on the ElementAttributes implementation
        this.targetAttributeDefinition = attributeDefinitions.forName(getTemplateMode(), "style");
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, Object expressionResult, IElementTagStructureHandler structureHandler) {
        if (context.containsVariable(MENU_ACTIVED) && (Boolean) context.getVariable(MENU_ACTIVED)) {
            String originStyle = tag.getAttributeValue("style");
            String toStyle;
            if (originStyle == null)
                toStyle = "display: block;";
            else
                toStyle = originStyle + "display: block;";

            StandardProcessorUtils.replaceAttribute(structureHandler, attributeName, this.targetAttributeDefinition
                    , "style", toStyle);
        }


    }


}
