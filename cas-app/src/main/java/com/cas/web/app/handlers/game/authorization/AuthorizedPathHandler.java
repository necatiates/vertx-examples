package com.cas.web.app.handlers.game.authorization;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by tolga on 18.03.2016.
 */
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


        static  AuthorizedPathHandler create(String root, ClassLoader classLoader) {
            return new AuthorizedPathHandlerImpl(root, classLoader);
        }


        AuthorizedPathHandler setAllowRootFileSystemAccess(boolean var1);


        AuthorizedPathHandler setWebRoot(String var1);


        AuthorizedPathHandler setFilesReadOnly(boolean var1);


        AuthorizedPathHandler setMaxAgeSeconds(long var1);


        AuthorizedPathHandler setCachingEnabled(boolean var1);


        AuthorizedPathHandler setDirectoryListing(boolean var1);


        AuthorizedPathHandler setIncludeHidden(boolean var1);


        AuthorizedPathHandler setCacheEntryTimeout(long var1);


        AuthorizedPathHandler setIndexPage(String var1);


        AuthorizedPathHandler setMaxCacheSize(int var1);


        AuthorizedPathHandler setAlwaysAsyncFS(boolean var1);


        AuthorizedPathHandler setEnableFSTuning(boolean var1);


        AuthorizedPathHandler setMaxAvgServeTimeNs(long var1);


        AuthorizedPathHandler setDirectoryTemplate(String var1);


        AuthorizedPathHandler setEnableRangeSupport(boolean var1);


        AuthorizedPathHandler setRole(String role);
    }
