package me.jiangcai.lib.bracket.thymeleaf.process;

import me.jiangcai.lib.bracket.thymeleaf.BracketDialect;
import me.jiangcai.lib.bracket.thymeleaf.BracketProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinition;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.standard.util.StandardProcessorUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.Validate;
import org.unbescape.html.HtmlEscape;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 根据输入的参数和activeMenu对比 如果在范围内则加上相应的标签
 *
 * @author Jolene
 */
public abstract class ClassesByActiveMenuProcessor extends AbstractStandardExpressionAttributeTagProcessor
        implements BracketProcessor, IAttributeDefinitionsAware {

    private static final Log log = LogFactory.getLog(ClassesByActiveMenuProcessor.class);

    public static final int ATTR_PRECEDENCE = 21100;

    public static final String MENU_ACTIVED = "me.jiangcai.menu.active";

    private AttributeDefinition targetAttributeDefinition;

    public ClassesByActiveMenuProcessor(String name) {
        super(TemplateMode.HTML, BracketDialect.PREFIX, name, ATTR_PRECEDENCE, false);
    }

    public void setAttributeDefinitions(final AttributeDefinitions attributeDefinitions) {
        Validate.notNull(attributeDefinitions, "Attribute Definitions cannot be null");
        // We precompute the AttributeDefinition of the target attribute in order to being able to use much
        // faster methods for setting/replacing attributes on the ElementAttributes implementation
        this.targetAttributeDefinition = attributeDefinitions.forName(getTemplateMode(), "class");
    }

    @Override
    protected final void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName, final String attributeValue,
            final Object expressionResult,
            final IElementTagStructureHandler structureHandler) {

        final String newAttributeValue = HtmlEscape.escapeHtml4Xml(expressionResult == null ? null : expressionResult.toString());
        if (newAttributeValue == null)
            return;

        structureHandler.setLocalVariable(MENU_ACTIVED, false);

        String[] value = newAttributeValue.split(",");
        Arrays.sort(value);

        // 先确认是否在范围之内
        String activeMenu = (String) context.getVariable("activeMenu");
        assert activeMenu != null;

        boolean shouldAppendClass = Arrays.binarySearch(value, activeMenu) != -1;

        if (!shouldAppendClass)
            return;
        //nav-parent

        // 确定应该使用哪一个class
        String appendClass;
        if (isParent(tag)) {
            structureHandler.setLocalVariable(MENU_ACTIVED, true);
            appendClass = "nav-active active";
        } else
            appendClass = "active";

        // append into current
        String originClass = tag.getAttributeValue("class");
        if (originClass != null)
            appendClass = originClass + " " + appendClass;

        StandardProcessorUtils.replaceAttribute(
                structureHandler, attributeName, this.targetAttributeDefinition, "class", appendClass);
        // These attributes might be "removable if empty", in which case we would simply remove the target attribute...
//        if (this.removeIfEmpty && (newAttributeValue == null || newAttributeValue.length() == 0)) {
//
//            // We are removing the equivalent attribute name, without the prefix...
//            structureHandler.removeAttribute(this.targetAttributeDefinition.getAttributeName());
//            structureHandler.removeAttribute(attributeName);
//
//        } else {

        // We are setting the equivalent attribute name, without the prefix...


//        }

    }

    private static final Pattern spacer = Pattern.compile("\\s+");

    private boolean isParent(IProcessableElementTag tag) {
        IAttribute clazz = tag.getAttribute("class");
        if (clazz == null)
            return false;
        if (clazz.getValue() == null)
            return false;
        String[] values = spacer.split(clazz.getValue());
        Arrays.sort(values);

        return Arrays.binarySearch(values, "na-parent") != -1;
    }
}
