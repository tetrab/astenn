using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Threading;

using Microsoft.SharePoint.Administration;
using Microsoft.SharePoint;

using Org.Lestr.Astenn.Plugin;
using Org.Lestr.Astenn.Configuration;

namespace Org.Lestr.Astenn.SharePoint
{


    public class SharePointPersistenceDriver : IPersistenceDriver
    {


        private const string KEY = "Astenn";


        private static SharePointPersistenceDriver singleton = new SharePointPersistenceDriver();


        public static SharePointPersistenceDriver Singleton
        {

            get { return singleton; }

        }// END Property SharePointPersistenceDriver


        private List<Thread> farm;
        private Dictionary<Thread, SPWebApplication> webApplication;
        private Dictionary<Thread, SPWeb> web;


        private SharePointPersistenceDriver()
        {

            farm = new List<Thread>();
            webApplication = new Dictionary<Thread, SPWebApplication>();
            web = new Dictionary<Thread, SPWeb>();

        }// END Constructor


        public void OpenSPFarm()
        {

            farm.Add(Thread.CurrentThread);

        }// END Method OpenSPFarm


        public void OpenSPWebApplication(SPWebApplication webApplication)
        {

            this.webApplication.Add(Thread.CurrentThread, webApplication);

        }// END Method OpenSPWebApplication


        public void OpenSPWeb(SPWeb web)
        {

            this.web.Add(Thread.CurrentThread, web);

        }// END Method OpenSPWeb


        public void Close()
        {

            if(farm.Contains(Thread.CurrentThread))
                farm.Remove(Thread.CurrentThread);

            if (webApplication.ContainsKey(Thread.CurrentThread))
                webApplication.Remove(Thread.CurrentThread);

            if (web.ContainsKey(Thread.CurrentThread))
                web.Remove(Thread.CurrentThread);

        }// END Method Close


        public void AddPluginInterface(string pluginInterfaceName)
        {

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                p.AddPluginInterface(pluginInterfaceName);

                web.Properties[KEY] = p.Document.InnerXml;
                web.Update();

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);
                
                p.AddPluginInterface(pluginInterfaceName);

                webApplication.Properties[KEY] = p.Document.InnerXml;
                webApplication.Update();

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                p.AddPluginInterface(pluginInterfaceName);

                localFarm.Properties[KEY] = p.Document.InnerXml;
                localFarm.Update();

            }

        }// END Method AddPluginInterface


        public void RemovePluginInterface(string pluginInterfaceName)
        {

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                p.RemovePluginInterface(pluginInterfaceName);

                web.Properties[KEY] = p.Document.InnerXml;
                web.Update();

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                p.RemovePluginInterface(pluginInterfaceName);

                webApplication.Properties[KEY] = p.Document.InnerXml;
                webApplication.Update();

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                p.RemovePluginInterface(pluginInterfaceName);

                localFarm.Properties[KEY] = p.Document.InnerXml;
                localFarm.Update();

            }

        }// END Method RemovePluginInterface


        public bool ExistPluginInterface(string pluginInterfaceName)
        {

            bool rslt = false;

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                rslt = p.ExistPluginInterface(pluginInterfaceName);

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                rslt = p.ExistPluginInterface(pluginInterfaceName);

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                rslt = p.ExistPluginInterface(pluginInterfaceName);

            }

            return rslt;

        }// END Method ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                p.AddPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

                web.Properties[KEY] = p.Document.InnerXml;
                web.Update();

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                p.AddPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

                webApplication.Properties[KEY] = p.Document.InnerXml;
                webApplication.Update();

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                p.AddPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

                localFarm.Properties[KEY] = p.Document.InnerXml;
                localFarm.Update();

            }

        }// END Method AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                p.RemovePluginImplementation(pluginInterfaceName, pluginImplementationAddress);

                web.Properties[KEY] = p.Document.InnerXml;
                web.Update();

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                p.RemovePluginImplementation(pluginInterfaceName, pluginImplementationAddress);

                webApplication.Properties[KEY] = p.Document.InnerXml;
                webApplication.Update();

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                p.RemovePluginImplementation(pluginInterfaceName, pluginImplementationAddress);

                localFarm.Properties[KEY] = p.Document.InnerXml;
                localFarm.Update();

            }

        }// END Method RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            bool rslt = false;

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                rslt = p.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                rslt = p.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                rslt = p.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

            }

            return rslt;

        }// END Method ExistPluginImplementation


        public IEnumerable<string> GetPluginInterfacesNames()
        {

            IEnumerable<string> rslt = new List<string>();

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                rslt = p.GetPluginInterfacesNames();

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                rslt = p.GetPluginInterfacesNames();

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                rslt = p.GetPluginInterfacesNames();

            }

            return rslt;

        }// END Method GetPluginInterfacesNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {

            IEnumerable<string> rslt = new List<string>();

            if (this.web.ContainsKey(Thread.CurrentThread))
            {

                SPWeb web = this.web[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(web);

                rslt = p.GetPluginImplementationsAddresses(pluginInterfaceName);

            }

            else if (this.webApplication.ContainsKey(Thread.CurrentThread))
            {

                SPWebApplication webApplication = this.webApplication[Thread.CurrentThread];
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(webApplication);

                rslt = p.GetPluginImplementationsAddresses(pluginInterfaceName);

            }

            else if (farm.Contains(Thread.CurrentThread))
            {

                SPFarm localFarm = SPFarm.Local;
                XMLDocumentPersistenceDriver p = GetXMLDocumentPersistenceDriver(localFarm);

                rslt = p.GetPluginImplementationsAddresses(pluginInterfaceName);

            }

            return rslt;

        }// END Method GetPluginImplementationsAddresses


        private XMLDocumentPersistenceDriver GetXMLDocumentPersistenceDriver(SPWebApplication webApplication)
        {

            XMLDocumentPersistenceDriver rslt;

            if (webApplication.Properties.ContainsKey(KEY))
            {
                XmlDocument document = new XmlDocument();
                document.LoadXml(webApplication.Properties[KEY].ToString());
                rslt = new XMLDocumentPersistenceDriver(document);
            }

            else
                rslt = new XMLDocumentPersistenceDriver();

            return rslt;

        }// END Method GetXMLDocumentPersistenceDriver


        private XMLDocumentPersistenceDriver GetXMLDocumentPersistenceDriver(SPWeb web)
        {

            XMLDocumentPersistenceDriver rslt;

            if (web.Properties.ContainsKey(KEY))
            {
                XmlDocument document = new XmlDocument();
                document.LoadXml(web.Properties[KEY].ToString());
                rslt = new XMLDocumentPersistenceDriver(document);
            }

            else
                rslt = new XMLDocumentPersistenceDriver();

            return rslt;

        }// END Method GetXMLDocumentPersistenceDriver


        private XMLDocumentPersistenceDriver GetXMLDocumentPersistenceDriver(SPFarm farm)
        {

            XMLDocumentPersistenceDriver rslt;

            if (farm.Properties.ContainsKey(KEY))
            {
                XmlDocument document = new XmlDocument();
                document.LoadXml(farm.Properties[KEY].ToString());
                rslt = new XMLDocumentPersistenceDriver(document);
            }

            else
                rslt = new XMLDocumentPersistenceDriver();

            return rslt;

        }// END Method GetXMLDocumentPersistenceDriver


    }// END Class SharePointPersistenceDriver


}// END Namespace Org.Lestr.Astenn.SharePoint
