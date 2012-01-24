using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using NUnit.Framework;

using Org.Lestr.Astenn.Plugin;
using Org.Lestr.Astenn.Configuration;

namespace Org.Lestr.Astenn.Test
{

    [TestFixture]
    public class TestPluginsManager
    {


        [SetUp]
        public void setUp() {

            PluginsManager.Singleton.Configuration.PersistenceDriver.ReadWritePersistenceDriver = new RAMPersistenceDriver();
            PluginsManager.Singleton.Configuration.PersistenceDriver.ReadOnlyPersistenceDrivers.Clear();
        
        }// END Method setUp


        [Test]
        public void test1()
        {
            Assert.False(PluginsManager.Singleton.GetRegisteredPlugins<IPlugin>().Count() > 0);

            PluginsManager.Singleton.RegisterPlugin(typeof(IPlugin), typeof(Plugin1));

            Assert.True(PluginsManager.Singleton.GetRegisteredPlugins<IPlugin>().Count() > 0);

            PluginsManager.Singleton.UnregisterPlugin(typeof(IPlugin), typeof(Plugin1));

            Assert.False(PluginsManager.Singleton.GetRegisteredPlugins<IPlugin>().Count() > 0);

        }// END Method test1


        [Test]
        public void test2()
        {

            Assert.Null(PluginsManager.Singleton.GetRegisteredSingletonPlugin<IPlugin>());

            PluginsManager.Singleton.RegisterSingletonPlugin(typeof(IPlugin), typeof(Plugin1));

            Assert.NotNull(PluginsManager.Singleton.GetRegisteredSingletonPlugin<IPlugin>());

            PluginsManager.Singleton.UnregisterSingletonPlugin(typeof(IPlugin));

            Assert.Null(PluginsManager.Singleton.GetRegisteredSingletonPlugin<IPlugin>());

        }// END Method test2


        [Test]
        public void test3()
        {

            PluginsManager.Singleton.RegisterPlugin(typeof(IPlugin), typeof(Plugin1));
            PluginsManager.Singleton.RegisterPlugin(typeof(IPlugin), typeof(Plugin2));

            IEnumerable<IPlugin> enumerator = PluginsManager.Singleton.GetRegisteredPlugins <IPlugin>();
            Assert.True(enumerator.Count() == 2);

        }// END Method test3


        [Test]
        public void test4()
        {

            PluginsManager.Singleton.RegisterSingletonPlugin(typeof(IPlugin), typeof(Plugin1));

            Assert.True(PluginsManager.Singleton.GetRegisteredSingletonPlugin<IPlugin>().GetType() == typeof(Plugin1));

            PluginsManager.Singleton.RegisterSingletonPlugin(typeof(IPlugin), typeof(Plugin2));

            Assert.True(PluginsManager.Singleton.GetRegisteredSingletonPlugin<IPlugin>().GetType() == typeof(Plugin2));

        }// END Method test4


    }// END Class TestPluginsManager


    public interface IPlugin
    {

        bool test1();

    }// END Interface IPlugin


    public class Plugin1 : IPlugin
    {

        public bool test1(){ return true; }

    }// END Class Plugin1


    public class Plugin2 : IPlugin
    {

        public bool test1(){ return true; }

    }// END Class Plugin2


}// END Namespace Org.Lestr.Astenn.Test