package ch.abertschi.sct.api;

public class SctConfigurator
{
    private static final SctConfigurator INSTANCE = new SctConfigurator();

    private static final String THREAD_LOCAL_CONFIG = "thread_local_config";

    private Configuration globalConfig;

    private SctConfigurator()
    {
    }

    public static SctConfigurator getInstance()
    {
        return INSTANCE;
    }

    public Configuration getConfiguration()
    {
        Configuration threadLocal = getThreadLocalConfiguration();
        return threadLocal == null ? getGlobalConfiguration() : threadLocal;
    }

    public SctConfigurator setGlobalConfiguration(Configuration config)
    {
        this.globalConfig = config;
        return this;
    }

    public Configuration getGlobalConfiguration()
    {
        return this.globalConfig;
    }

    public SctConfigurator setThreadLocalConfiguration(Configuration config)
    {
        ThreadLocalContextHolder.put(THREAD_LOCAL_CONFIG, config);
        return this;
    }

    public Configuration getThreadLocalConfiguration()
    {
        Object o = ThreadLocalContextHolder.get(THREAD_LOCAL_CONFIG);
        return o == null ? null : (Configuration) o;
    }

}
