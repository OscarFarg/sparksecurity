/*
 * Copyright (c) 2014. Oxborrow.nl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.oxborrow.spark.security.exceptions;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

/**
 * Date: 27-11-14
 *
 * @author oscar
 */
public class MustacheTemplateExceptionHandler implements ExceptionHandler {

    public static final String FORBIDDEN = "nl.oxborrow.nucleuswise.security.exceptions.ForbiddenException";
    public static final String UNAUTHORIZED = "nl.oxborrow.nucleuswise.security.exceptions.UnauthorizedException";
    public static final int SERVER_ERROR = 500;

    private String template401;
    private String template403;
    private String template500;

    @Inject
    public MustacheTemplateExceptionHandler(@Named("template.error.401") String template401,
                                            @Named("template.error.403") String template403,
                                            @Named("template.error.500") String template500) {
        this.template401 = template401;
        this.template403 = template403;
        this.template500 = template500;
    }

    private TemplateEngine templateEngine = new MustacheTemplateEngine();

    @Override
    public void handle(Exception exception, Request request, Response response) {

        ModelAndView view;
        int code;
        switch (exception.getClass().getName()) {
            case FORBIDDEN:
                view = new ModelAndView(null, template403);
                code = ForbiddenException.CODE;
                break;
            case UNAUTHORIZED:
                view = new ModelAndView(null, template401);
                code = UnauthorizedException.CODE;
                break;
            default:
                view = new ModelAndView(null, template500);
                code = SERVER_ERROR;
        }

        String render = templateEngine.render(view);
        response.status(code);
        response.body(render);
    }
}
