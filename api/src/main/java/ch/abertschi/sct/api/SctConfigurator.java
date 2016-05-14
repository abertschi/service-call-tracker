package ch.abertschi.sct.api;

public class SctConfigurator
{
    private static final SctConfigurator INSTANCE = new SctConfigurator();

    private static final String THREAD_LOCAL_CONFIG = "thread_local_config";

    private SctConfiguration globalConfig;

    private SctConfigurator()
    {
    }

    public static SctConfigurator getInstance()
    {
        return INSTANCE;
    }

    public SctConfiguration getConfiguration()
    {
        SctConfiguration threadLocal = getThreadLocalConfiguration();
        return threadLocal == null ? getGlobalConfiguration() : threadLocal;
    }

    public SctConfigurator setGlobalConfiguration(SctConfiguration config)
    {
        this.globalConfig = config;
        return this;
    }

    public SctConfiguration getGlobalConfiguration()
    {
        return this.globalConfig;
    }

    public SctConfigurator setThreadLocalConfiguration(SctConfiguration config)
    {
        ThreadLocalContextHolder.put(THREAD_LOCAL_CONFIG, config);
        return this;
    }

    public SctConfiguration getThreadLocalConfiguration()
    {
        Object o = ThreadLocalContextHolder.get(THREAD_LOCAL_CONFIG);
        return o == null ? null : (SctConfiguration) o;
    }

}
