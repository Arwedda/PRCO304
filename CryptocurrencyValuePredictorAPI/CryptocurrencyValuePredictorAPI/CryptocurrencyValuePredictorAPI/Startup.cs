using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(CryptocurrencyValuePredictorAPI.Startup))]
namespace CryptocurrencyValuePredictorAPI
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
