from django.conf.urls import patterns, include, url
from django.views.generic import TemplateView
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'hackmyride2.views.home', name='home'),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^oauth2/', include('provider.oauth2.urls', namespace='oauth2')),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^api/', include('transitrestapi.urls')),
    url(r'^api/', include('realtimerestapi.urls')),
    url(r'^api-doc/', include('rest_framework_swagger.urls')),
    url(r'^$', TemplateView.as_view(template_name="explore/home.html"),
        name='home'),
    url(r'', include('exploreapp.urls'))
)
