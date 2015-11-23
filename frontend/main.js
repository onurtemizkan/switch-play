/**
 * Frontend component of the switch project
 * @author Onur Temizkan
 * @version 1.0.0
 */

(function() {
  "use strict";

  /**
   * Global Toggle-Node List
   */
  var nodeList = [];

  /**
   * Configuration
   */
  var conf = function() {
    return {
      request: {
        method: "GET",
        url: "http://localhost:9000/"
      }
    };
  };

  /**
   * XHR Request to given url with given method
   * @param {string} method - The method to be used to make request
   * @param {string} url - The url of the server to be requested
   * @returns {Promise} returns the promise for the result of the request
   */
  var makeRequest = function(method, url) {
    return new Promise(function(resolve, reject) {
      var xhr = new XMLHttpRequest();
      xhr.open(method, url);
      xhr.onload = function() {
        if (this.status >= 200 && this.status < 300) {
          resolve(xhr.response);
        } else {
          reject({
            status: this.status,
            statusText: xhr.statusText
          });
          // Rejected!
        }
      };
      xhr.onerror = function() {
        reject({
          status: this.status,
          statusText: xhr.statusText
        });
      };
      xhr.send();
    });
  };

  /**
   * Finds the given element and returns f it is active
   * @param {string} projectName - Name of the project to be found
   * @param {array} projectList - Array of projects to be searched
   */
  var checkActive = function(projectName) {
    var ret = nodeList.find(function(element, index, array) {
      if (element.name === projectName) {
        return true;
      }
      return false;      
    });
    if (ret != undefined) {
      return ret.isActive;
    }
    return "Node Not Found!";
  };
    
  /**
   * Calls an XHR request to fill-up the project list
   * @param {string} projectName - Name of the project to be found
   */
  var init = function() {
    makeRequest(conf().request.method, conf().request.url)
      .then(function(data) {
        var obj = JSON.parse(data);
        Object.keys(obj).forEach(function(key) {
          nodeList.push({name: key, isActive: obj[key]});
        });
      });
  };

  init();

  /**
   * Assertions
   */  
  window.setTimeout(function() {
    console.log(checkActive("david"));
    console.log(checkActive("gilmour"));
    console.log(checkActive("hola"));
  }, 3000);
})();
