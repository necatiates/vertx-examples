package com.cas.web.app.handlers;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.StaticHandlerImpl;

/**
 * Created by tolga on 18.03.2016.
 */
@VertxGen
public interface AuthorizedPathHandler extends Handler<RoutingContext>{

        String DEFAULT_WEB_ROOT = "webroot";
        boolean DEFAULT_FILES_READ_ONLY = true;
        long DEFAULT_MAX_AGE_SECONDS = 86400L;
        boolean DEFAULT_CACHING_ENABLED = true;
        boolean DEFAULT_DIRECTORY_LISTING = false;
        String DEFAULT_DIRECTORY_TEMPLATE = "vertx-web-directory.html";
        boolean DEFAULT_INCLUDE_HIDDEN = true;
        long DEFAULT_CACHE_ENTRY_TIMEOUT = 30000L;
        String DEFAULT_INDEX_PAGE = "/index.html";
        int DEFAULT_MAX_CACHE_SIZE = 10000;
        boolean DEFAULT_ALWAYS_ASYNC_FS = false;
        boolean DEFAULT_ENABLE_FS_TUNING = true;
        long DEFAULT_MAX_AVG_SERVE_TIME_NS = 1000000L;
        boolean DEFAULT_RANGE_SUPPORT = true;
        boolean DEFAULT_ROOT_FILESYSTEM_ACCESS = false;
        String DEFAULT_ROLE = "user";

        static AuthorizedPathHandler create() {
            return new AuthorizedPathHandlerImpl();
        }

        static AuthorizedPathHandler create(String root) {
            return new AuthorizedPathHandlerImpl(root, (ClassLoader)null);
        }

        @GenIgnore
        static  AuthorizedPathHandler create(String root, ClassLoader classLoader) {
            return new AuthorizedPathHandlerImpl(root, classLoader);
        }

        @Fluent
        AuthorizedPathHandler setAllowRootFileSystemAccess(boolean var1);

        @Fluent
        AuthorizedPathHandler setWebRoot(String var1);

        @Fluent
        AuthorizedPathHandler setFilesReadOnly(boolean var1);

        @Fluent
        AuthorizedPathHandler setMaxAgeSeconds(long var1);

        @Fluent
        AuthorizedPathHandler setCachingEnabled(boolean var1);

        @Fluent
        AuthorizedPathHandler setDirectoryListing(boolean var1);

        @Fluent
        AuthorizedPathHandler setIncludeHidden(boolean var1);

        @Fluent
        AuthorizedPathHandler setCacheEntryTimeout(long var1);

        @Fluent
        AuthorizedPathHandler setIndexPage(String var1);

        @Fluent
        AuthorizedPathHandler setMaxCacheSize(int var1);

        @Fluent
        AuthorizedPathHandler setAlwaysAsyncFS(boolean var1);

        @Fluent
        AuthorizedPathHandler setEnableFSTuning(boolean var1);

        @Fluent
        AuthorizedPathHandler setMaxAvgServeTimeNs(long var1);

        @Fluent
        AuthorizedPathHandler setDirectoryTemplate(String var1);

        @Fluent
        AuthorizedPathHandler setEnableRangeSupport(boolean var1);

        @Fluent
        AuthorizedPathHandler setRole(String role);
    }
