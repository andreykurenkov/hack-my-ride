from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from transittimesapp.models import TransitSystem
from transittimesapp.serializers import TransitSystemSerializer
# Create your views here.

@api_view(['GET', 'POST'])
def transit_system(request):
    """
    List all snippets, or create a new snippet.
    """
    if request.method == 'GET':
        #snippets = Snippet.objects.all()
        #serializer = SnippetSerializer(snippets, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = TransitSystemSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
