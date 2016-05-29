package me.jiangcai.lib.bracket.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExecutionAttributeDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jolene
 */
@Component
public class BracketDialect extends AbstractProcessorDialect implements IExecutionAttributeDialect {

    private final Set<IProcessor> processors = new HashSet<>();

    public static final String PREFIX = "bkt";

    @Autowired
    public BracketDialect(Set<BracketProcessor> processors) {
        super("Bracket", PREFIX, 1000);
        processors.forEach(this.processors::add);
        // to process xmlns
        this.processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, PREFIX));
    }

    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return processors;
    }

    public Map<String, Object> getExecutionAttributes() {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("config", appService.currentSystemConfig());
//        map.put("resourceService", resourceService);
//        if (processingContext.getContext() != null && processingContext.getContext() instanceof WebContext) {
//            // 另外声明几个静态变量 以标识当前登录的身份
//            Authentication authentication = AuthUtils.getAuthenticationObject();
//            if (authentication.getPrincipal() instanceof Login) {
//                Login login = (Login) authentication.getPrincipal();
//                map.put("login", login);
//                map.put("newEmails", emailRepository.findByBelongAndReadFalse(login));
//
//                map.put("isLogin", true);
//                if (authentication.getPrincipal() instanceof User) {
//                    User user = (User) authentication.getPrincipal();
//                    map.put("isEndUser", true);
//
//                    map.put("user", user);
//                    map.put("ticketCount", ticketRepository.countByUsedFalseAndClaimant(user));
//                    map.put("newUsers", userRepository.findByGuideAndGuideKnowMeFalse(user));
//
//                } else {
//                    map.put("isEndUser", false);
//                }
//            } else {
//                map.put("isEndUser", false);
//                map.put("isLogin", false);
//            }
//
//        }
        return map;
    }
}
